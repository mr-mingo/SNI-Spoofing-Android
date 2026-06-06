package com.example.proxy

import kotlinx.coroutines.*
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class SniProxyServer(
    private val config: ProxyConfig,
    private val onLog: (String) -> Unit,
    private val onStatsUpdate: (tx: Long, rx: Long) -> Unit,
    private val onConnectionCount: (active: Int, total: Int) -> Unit
) {
    private var serverSocket: ServerSocket? = null
    var isRunning = false
        private set
    private val proxyDispatcher = java.util.concurrent.Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val scope = CoroutineScope(proxyDispatcher + SupervisorJob())
    private val activeConnections = AtomicInteger(0)
    private val totalConnections = AtomicInteger(0)
    
    private val totalTx = AtomicLong(0)
    private val totalRx = AtomicLong(0)

    fun start() {
        if (isRunning) return
        isRunning = true
        totalTx.set(0)
        totalRx.set(0)
        onLog("[SYSTEM] Starting proxy server on ${config.listenHost}:${config.listenPort}...")
        
        scope.launch {
            try {
                serverSocket = ServerSocket(config.listenPort, 50, java.net.InetAddress.getByName(config.listenHost))
                onLog("[SUCCESS] Proxy active on loopback: ${config.listenHost}:${config.listenPort}")
                onLog("[TUNNEL] Routing traffic to -> ${config.connectHost}:${config.connectPort}")
                onLog("[STRATEGY] Method: ${config.bypassMethod} | Mock/Fake SNI: ${config.fakeSni}")
                
                while (isRunning) {
                    val clientSocket = serverSocket?.accept() ?: break
                    val currentActive = activeConnections.incrementAndGet()
                    val currentTotal = totalConnections.incrementAndGet()
                    onConnectionCount(currentActive, currentTotal)
                    
                    launch {
                        handleClient(clientSocket)
                    }
                }
            } catch (e: Exception) {
                if (isRunning) {
                    onLog("[ERROR] Server listener error: ${e.message}")
                }
            } finally {
                stop()
            }
        }
    }

    fun stop() {
        if (!isRunning) return
        isRunning = false
        onLog("[SYSTEM] Stopping SNI spoofing server...")
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            // Socket closed exception ignored
        }
        serverSocket = null
        scope.cancel()
        activeConnections.set(0)
        onConnectionCount(0, totalConnections.get())
        onLog("[SYSTEM] Proxy stopped. Idle state entered.")
    }

    private suspend fun handleClient(clientSocket: Socket) {
        val clientIp = clientSocket.remoteSocketAddress.toString().removePrefix("/")
        onLog("[CONN] Accepted flow from client: $clientIp")
        
        var targetSocket: Socket? = null
        try {
            targetSocket = Socket(config.connectHost, config.connectPort)
            targetSocket.tcpNoDelay = true
            clientSocket.tcpNoDelay = true
            
            val clientIn = clientSocket.getInputStream()
            val clientOut = clientSocket.getOutputStream()
            val targetIn = targetSocket.getInputStream()
            val targetOut = targetSocket.getOutputStream()
            
            coroutineScope {
                // Outbound flow: Client -> Target (with DPI bypass filter)
                val outboundJob = launch {
                    try {
                        relayOutbound(clientIn, targetOut, clientIp)
                    } catch (e: Exception) {
                        // Suppress connection-reset/pipe closures
                    }
                }
                
                // Inbound flow: Target -> Client
                val inboundJob = launch {
                    try {
                        relayInbound(targetIn, clientOut, clientIp)
                    } catch (e: Exception) {
                        // Suppress connection-reset/pipe closures
                    }
                }
                
                joinAll(outboundJob, inboundJob)
            }
        } catch (e: Exception) {
            onLog("[ERROR] Fwd failed to ${config.connectHost}:${config.connectPort} for $clientIp: ${e.message}")
        } finally {
            try { clientSocket.close() } catch (e: Exception) {}
            try { targetSocket?.close() } catch (e: Exception) {}
            
            var currentActive = activeConnections.decrementAndGet()
            if (currentActive < 0) {
                activeConnections.set(0)
                currentActive = 0
            }
            if (isRunning) {
                onConnectionCount(currentActive, totalConnections.get())
            }
            onLog("[CONN] Terminated session: $clientIp")
        }
    }

    private suspend fun relayOutbound(input: InputStream, output: OutputStream, clientIp: String) {
        val buffer = ByteArray(16384)
        var bytesRead: Int
        var isFirstPacket = true
        
        while (true) {
            if (!scope.isActive) break
            bytesRead = try {
                input.read(buffer)
            } catch (e: Exception) {
                -1
            }
            if (bytesRead == -1) break
            
            if (isFirstPacket && bytesRead > 0) {
                isFirstPacket = false
                // Check if TLS ClientHello: starts with 0x16, version major is 0x03
                if (bytesRead >= 5 && buffer[0] == 0x16.toByte() && buffer[1] == 0x03.toByte()) {
                    if (config.bypassMethod == "TCP_FRAGMENTATION") {
                        onLog("[BYPASS] Handshake detected (SSLv3/TLS-Record type 22)! Splitting TCP flight for $clientIp...")
                        
                        // Divide the TLS ClientHello record across two distinct writes
                        val splitIndex = 3 // Split after TLS Header
                        val splitPart1 = buffer.copyOfRange(0, splitIndex)
                        val splitPart2 = buffer.copyOfRange(splitIndex, bytesRead)
                        
                        try {
                            output.write(splitPart1)
                            output.flush()
                            
                            // Delay of ~35ms guarantees transport splits them into 2 network segments,
                            // confusing middlebox parsers looking for immediate SNI frames
                            delay(35)
                            
                            output.write(splitPart2)
                            output.flush()
                        } catch (e: Exception) {}
                        
                        val tx = totalTx.addAndGet(bytesRead.toLong())
                        onStatsUpdate(tx, totalRx.get())
                        onLog("[BYPASS] Fragmented ClientHello completed: Segment 1 ($splitIndex B) + Segment 2 (${bytesRead - splitIndex} B)")
                        continue
                    } else {
                        onLog("[TUNNEL] Outbound ClientHello forward direct for $clientIp (No bypass technique applied).")
                    }
                } else if (config.bypassMethod == "TCP_FRAGMENTATION" && bytesRead > 0) {
                    // Fragment generic non-TLS outbound records
                    val splitIndex = if (bytesRead > 5) 5 else 1
                    val splitPart1 = buffer.copyOfRange(0, splitIndex)
                    val splitPart2 = buffer.copyOfRange(splitIndex, bytesRead)
                    try {
                        output.write(splitPart1)
                        output.flush()
                        delay(20)
                        output.write(splitPart2)
                        output.flush()
                    } catch (e: Exception) {}
                    val tx = totalTx.addAndGet(bytesRead.toLong())
                    onStatsUpdate(tx, totalRx.get())
                    continue
                }
            }
            
            if (bytesRead > 0) {
                try {
                    output.write(buffer, 0, bytesRead)
                    output.flush()
                } catch (e: Exception) {}
                val tx = totalTx.addAndGet(bytesRead.toLong())
                onStatsUpdate(tx, totalRx.get())
            }
        }
    }

    private suspend fun relayInbound(input: InputStream, output: OutputStream, clientIp: String) {
        val buffer = ByteArray(16384)
        var bytesRead: Int
        while (true) {
            if (!scope.isActive) break
            bytesRead = try {
                input.read(buffer)
            } catch (e: Exception) {
                -1
            }
            if (bytesRead == -1) break
            if (bytesRead > 0) {
                try {
                    output.write(buffer, 0, bytesRead)
                    output.flush()
                } catch (e: Exception) {}
                val rx = totalRx.addAndGet(bytesRead.toLong())
                onStatsUpdate(totalTx.get(), rx)
            }
        }
    }
}
