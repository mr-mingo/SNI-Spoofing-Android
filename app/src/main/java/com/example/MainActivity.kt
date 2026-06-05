package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxy.ProxyConfig
import com.example.proxy.SniProxyServer
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var proxyServer: SniProxyServer? = null

    // State parameters held at Activity level
    private var isProxyRunningState = mutableStateOf(false)
    private var activeConnectionsCount = mutableStateOf(0)
    private var txBytesState = mutableStateOf(0L)
    private var rxBytesState = mutableStateOf(0L)
    private val consoleLogsList = mutableStateListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Setup initial system log
        addLog("[SYSTEM] Application initialized. Standing by.")
        addLog("[INFO] Standard loopback binding is prepared on 127.0.0.1")

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF07090E) // Premium deep background
                ) {
                    AppRootScreen(
                        isProxyRunning = isProxyRunningState.value,
                        activeConnections = activeConnectionsCount.value,
                        txBytes = txBytesState.value,
                        rxBytes = rxBytesState.value,
                        logs = consoleLogsList,
                        onToggleProxy = { config -> toggleProxy(config) },
                        onClearLogs = { consoleLogsList.clear() }
                    )
                }
            }
        }
    }

    private fun toggleProxy(config: ProxyConfig) {
        val server = proxyServer
        if (server != null && server.isRunning) {
            // Stop server
            server.stop()
            proxyServer = null
            isProxyRunningState.value = false
            addLog("[SYSTEM] Proxy terminated by user.")
        } else {
            // Start server
            txBytesState.value = 0L
            rxBytesState.value = 0L
            activeConnectionsCount.value = 0
            
            val newServer = SniProxyServer(
                config = config,
                onLog = { message ->
                    runOnUiThread {
                        addLog(message)
                    }
                },
                onStatsUpdate = { tx, rx ->
                    runOnUiThread {
                        txBytesState.value = tx
                        rxBytesState.value = rx
                    }
                },
                onConnectionCount = { count ->
                    runOnUiThread {
                        activeConnectionsCount.value = count
                    }
                }
            )
            proxyServer = newServer
            newServer.start()
            isProxyRunningState.value = newServer.isRunning
        }
    }

    private fun addLog(message: String) {
        val timeStamp = java.text.SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(java.util.Date())
        if (consoleLogsList.size > 200) {
            consoleLogsList.removeAt(0)
        }
        consoleLogsList.add("[$timeStamp] $message")
    }

    override fun onDestroy() {
        super.onDestroy()
        proxyServer?.stop()
    }
}

@Composable
fun AppRootScreen(
    isProxyRunning: Boolean,
    activeConnections: Int,
    txBytes: Long,
    rxBytes: Long,
    logs: List<String>,
    onToggleProxy: (ProxyConfig) -> Unit,
    onClearLogs: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Default system config state
    var listenPort by remember { mutableStateOf("40443") }
    var connectHost by remember { mutableStateOf("188.114.98.0") }
    var connectPort by remember { mutableStateOf("443") }
    var fakeSni by remember { mutableStateOf("auth.vercel.com") }
    var bypassMethod by remember { mutableStateOf("TCP_FRAGMENTATION") }

    val activeConfig = remember(listenPort, connectHost, connectPort, fakeSni, bypassMethod) {
        ProxyConfig(
            listenHost = "127.0.0.1",
            listenPort = listenPort.toIntOrNull() ?: 40443,
            connectHost = connectHost.trim(),
            connectPort = connectPort.toIntOrNull() ?: 443,
            fakeSni = fakeSni.trim(),
            bypassMethod = bypassMethod
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF07090E),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0D111A),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .border(1.dp, Color(0xFF1E2638), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00FFCC),
                        selectedTextColor = Color(0xFF00FFCC),
                        unselectedIconColor = Color(0xFF8D9CB0),
                        unselectedTextColor = Color(0xFF8D9CB0),
                        indicatorColor = Color(0xFF131E31)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Config", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00FFCC),
                        selectedTextColor = Color(0xFF00FFCC),
                        unselectedIconColor = Color(0xFF8D9CB0),
                        unselectedTextColor = Color(0xFF8D9CB0),
                        indicatorColor = Color(0xFF131E31)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Simulator") },
                    label = { Text("How it Works", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00FFCC),
                        selectedTextColor = Color(0xFF00FFCC),
                        unselectedIconColor = Color(0xFF8D9CB0),
                        unselectedTextColor = Color(0xFF8D9CB0),
                        indicatorColor = Color(0xFF131E31)
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Elegant Header bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0C0F16))
                    .border(width = 1.dp, color = Color(0xFF181F2F))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(
                                        color = if (isProxyRunning) Color(0xFF00FFCC) else Color(0xFFFF496B),
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "SNI SPOOFING TUNNEL",
                                letterSpacing = 2.sp,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Text(
                            "Secure DPI Bypass Sandbox - Android Port",
                            color = Color(0xFF8E9AAA),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Power Toggle Button in Header
                    Button(
                        onClick = { onToggleProxy(activeConfig) },
                        modifier = Modifier.testTag("start_stop_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isProxyRunning) Color(0xFFFF2E56) else Color(0xFF00E6FF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (isProxyRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                            contentDescription = if (isProxyRunning) "Stop Tunnel" else "Start Tunnel",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isProxyRunning) "STOP" else "START",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Screen Switcher with Fade animations
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                    },
                    label = "TabTransition"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> DashboardScreen(
                            isProxyRunning = isProxyRunning,
                            activeConnections = activeConnections,
                            txBytes = txBytes,
                            rxBytes = rxBytes,
                            logs = logs,
                            activeConfig = activeConfig,
                            onClearLogs = onClearLogs
                        )
                        1 -> SettingsScreen(
                            listenPort = listenPort,
                            onListenPortChange = { listenPort = it },
                            connectHost = connectHost,
                            onConnectHostChange = { connectHost = it },
                            connectPort = connectPort,
                            onConnectPortChange = { connectPort = it },
                            fakeSni = fakeSni,
                            onFakeSniChange = { fakeSni = it },
                            bypassMethod = bypassMethod,
                            onBypassMethodChange = { bypassMethod = it },
                            isProxyRunning = isProxyRunning
                        )
                        2 -> ExplainerScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    isProxyRunning: Boolean,
    activeConnections: Int,
    txBytes: Long,
    rxBytes: Long,
    logs: List<String>,
    activeConfig: ProxyConfig,
    onClearLogs: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    // Auto scroll logs to newest
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            scrollState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Network Performance Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Speedo/Transmission Card
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1420)),
                border = BorderStroke(1.dp, Color(0xFF1B2236))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow, // Upload arrow
                            contentDescription = "TX Data",
                            tint = Color(0xFF00FFCC),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Transmitted (TX)", color = Color(0xFF8F9FBC), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = bytesToSymbolicString(txBytes),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // RX Card
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1420)),
                border = BorderStroke(1.dp, Color(0xFF1B2236))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh, // Download arrow
                            contentDescription = "RX Data",
                            tint = Color(0xFF00BFA5),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Received (RX)", color = Color(0xFF8F9FBC), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = bytesToSymbolicString(rxBytes),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Info Summary Block
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1420)),
            border = BorderStroke(1.dp, Color(0xFF232D47))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ACTIVE FLOWS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A72A4), letterSpacing = 1.sp)
                    Text(
                        "$activeConnections session(s)", 
                        color = if (activeConnections > 0) Color(0xFF00FFCC) else Color.White,
                        fontSize = 15.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Divider(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp),
                    color = Color(0xFF232D47)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text("TARGET ADDRESS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A72A4), letterSpacing = 1.sp)
                    Text(
                        "${activeConfig.connectHost}:${activeConfig.connectPort}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Console Log Terminal Layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Console",
                    tint = Color(0xFF00FFCC),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "REAL-TIME DPI BYPASS TERMINAL LOGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF8D9CB0),
                    letterSpacing = 1.sp
                )
            }
            Text(
                "CLEAR LOGS",
                color = Color(0xFFFF5271),
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .clickable { onClearLogs() }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Live Log Terminal window
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF040609)),
            border = BorderStroke(1.dp, Color(0xFF1F293D)),
            shape = RoundedCornerShape(6.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                if (logs.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            ">_ Terminal Idle",
                            color = Color(0xFF323B4C),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Start the tunnel to intercept & fragment local flows",
                            color = Color(0xFF323B4C),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(logs) { log ->
                            Text(
                                text = log,
                                color = getLogColor(log),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        // Android proxy routing tip
        val clipboardManager = LocalClipboardManager.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0E131F), RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF1E263B), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "💡 How to connect your phone traffic to this Proxy:",
                    color = Color(0xFF00FFCC),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString("127.0.0.1:${activeConfig.listenPort}"))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E2F4C),
                        contentColor = Color(0xFF00FFCC)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Copy Proxy",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Configure your Android Wi-Fi proxy settings, telegram proxy (Socks5/MTProto), or other network apps to point to Host: 127.0.0.1 and Port: ${activeConfig.listenPort}. The server will split outbound TLS flights securely on the fly. You can customize the ports, endpoint IP, and WHITELIST target SNI completely in the 'Config' tab!",
                color = Color(0xFFC5D2E5),
                lineHeight = 15.sp,
                fontSize = 11.sp
            )
        }
    }
}

private fun getLogColor(text: String): Color {
    return when {
        text.contains("[ERROR]") -> Color(0xFFFF5271)
        text.contains("[SUCCESS]") -> Color(0xFF00FFCC)
        text.contains("[BYPASS]") -> Color(0xFFFFEB3B)
        text.contains("[SYSTEM]") -> Color(0xFF00E6FF)
        text.contains("[CONN]") -> Color(0xFF9CCC65)
        else -> Color(0xFFB0BEC5)
    }
}

private fun bytesToSymbolicString(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val exp = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
    val pre = "KMGTPE"[exp - 1] + ""
    return String.format(Locale.getDefault(), "%.2f %sB", bytes / Math.pow(1024.0, exp.toDouble()), pre)
}

@Composable
fun SettingsScreen(
    listenPort: String,
    onListenPortChange: (String) -> Unit,
    connectHost: String,
    onConnectHostChange: (String) -> Unit,
    connectPort: String,
    onConnectPortChange: (String) -> Unit,
    fakeSni: String,
    onFakeSniChange: (String) -> Unit,
    bypassMethod: String,
    onBypassMethodChange: (String) -> Unit,
    isProxyRunning: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "TUNNEL PROXY CONFIGURATION",
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
            Text(
                "Configure bypass routing parameters. Changes apply to the next session restart.",
                color = Color(0xFF8A98B0),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Local listen port field
        item {
            OutlinedTextField(
                value = listenPort,
                onValueChange = onListenPortChange,
                enabled = !isProxyRunning,
                label = { Text("Local Proxy Listen Port", color = Color(0xFF7D91B2)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("listen_port_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00FFCC),
                    unfocusedBorderColor = Color(0xFF26334F),
                    focusedLabelColor = Color(0xFF00FFCC),
                    unfocusedLabelColor = Color(0xFF8A98B0),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        // Connect Target host
        item {
            OutlinedTextField(
                value = connectHost,
                onValueChange = onConnectHostChange,
                enabled = !isProxyRunning,
                label = { Text("Target Remote Host / IP address", color = Color(0xFF7D91B2)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("target_host_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00FFCC),
                    unfocusedBorderColor = Color(0xFF26334F),
                    focusedLabelColor = Color(0xFF00FFCC),
                    unfocusedLabelColor = Color(0xFF8A98B0),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
        }

        // Target Port
        item {
            OutlinedTextField(
                value = connectPort,
                onValueChange = onConnectPortChange,
                enabled = !isProxyRunning,
                label = { Text("Target Port (HTTPS/TLS is usually 443)", color = Color(0xFF7D91B2)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00FFCC),
                    unfocusedBorderColor = Color(0xFF26334F),
                    focusedLabelColor = Color(0xFF00FFCC),
                    unfocusedLabelColor = Color(0xFF8A98B0),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        // Spoof SNI Target
        item {
            OutlinedTextField(
                value = fakeSni,
                onValueChange = onFakeSniChange,
                enabled = !isProxyRunning,
                label = { Text("Decoy SNI Hostname (Allowed Whitelist Domain)", color = Color(0xFF7D91B2)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fake_sni_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00FFCC),
                    unfocusedBorderColor = Color(0xFF26334F),
                    focusedLabelColor = Color(0xFF00FFCC),
                    unfocusedLabelColor = Color(0xFF8A98B0),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
            Text(
                "The server injects this whitelisted host (e.g. mci.ir, telewebion.com, standard DNS targets) in packet streams to satisfy DPI passive string checkers.",
                color = Color(0xFF6E7E9C),
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }

        // Bypass Strategy Picker
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Bypass Strategy Selection:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8DABDF),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D121B), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF222B3D), RoundedCornerShape(8.dp))
            ) {
                // Choice A: TCP FRAGMENTATION
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isProxyRunning) { onBypassMethodChange("TCP_FRAGMENTATION") }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = bypassMethod == "TCP_FRAGMENTATION",
                        onClick = { onBypassMethodChange("TCP_FRAGMENTATION") },
                        enabled = !isProxyRunning,
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00FFCC))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Split Handshake Flight (TCP Segmentation)", 
                            color = Color.White, 
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "No Root Required. Segments TLS ClientHello headers to confuse passive DPI parsers without raw drivers.", 
                            color = Color(0xFF8E9EB9), 
                            fontSize = 10.sp
                        )
                    }
                }
                
                Divider(color = Color(0xFF1E283A))

                // Choice B: DIRECT TUNNEL
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isProxyRunning) { onBypassMethodChange("DIRECT_TUNNEL") }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = bypassMethod == "DIRECT_TUNNEL",
                        onClick = { onBypassMethodChange("DIRECT_TUNNEL") },
                        enabled = !isProxyRunning,
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00FFCC))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Direct Port Forwarding (Pass-through)", 
                            color = Color.White, 
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Disable bypass filters. Forward standard TCP payload stream completely unmodified.", 
                            color = Color(0xFF8E9EB9), 
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
        
        if (isProxyRunning) {
            item {
                Text(
                    "⚠️ Stop the active proxy server session to edit configurations.",
                    color = Color(0xFFFF5271),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        }
    }
}

// Custom interactive simulator Composable representing how DPI Bypass operates
@Composable
fun ExplainerScreen() {
    var stepIndex by remember { mutableStateOf(1) }
    var selectedMethodTab by remember { mutableStateOf(0) } // 0: Python WinDivert, 1: Android Split

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            "INTERACTIVE DPI BYPASS SIMULATOR",
            fontWeight = FontWeight.Black,
            color = Color.White,
            fontSize = 14.sp,
            letterSpacing = 1.sp
        )
        Text(
            "Compare the original Windows WinDivert mechanism (requires kernel privileges) with our mobile Android User-space handshake fragmentation strategy.",
            color = Color(0xFF8EAECE),
            lineHeight = 14.sp,
            fontSize = 11.sp
        )

        // Tab Selector for methods
        TabRow(
            selectedTabIndex = selectedMethodTab,
            containerColor = Color(0xFF0C101A),
            contentColor = Color(0xFF00FFCC),
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).border(1.dp, Color(0xFF212A3D), RoundedCornerShape(8.dp))
        ) {
            Tab(
                selected = selectedMethodTab == 0,
                onClick = { selectedMethodTab = 0; stepIndex = 1 },
                text = { Text("Python: Wrong TCP Seq", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
            )
            Tab(
                selected = selectedMethodTab == 1,
                onClick = { selectedMethodTab = 1; stepIndex = 1 },
                text = { Text("Android: Handshake Split", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
            )
        }

        // Animated State diagram
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF04060A), RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF1B2336), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Draw Nodes
                val yMid = height * 0.45f
                val clientX = width * 0.15f
                val dpiX = width * 0.5f
                val serverX = width * 0.85f

                // Client local box background
                drawCircle(color = Color(0xFF13223A), radius = 24.dp.toPx(), center = Offset(clientX, yMid))
                // DPI
                drawCircle(color = Color(0xFF331621), radius = 24.dp.toPx(), center = Offset(dpiX, yMid))
                // Server
                drawCircle(color = Color(0xFF122C24), radius = 24.dp.toPx(), center = Offset(serverX, yMid))

                // Connection Paths
                drawLine(
                    color = Color(0xFF1E273A),
                    start = Offset(clientX + 24.dp.toPx(), yMid),
                    end = Offset(dpiX - 24.dp.toPx(), yMid),
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
                drawLine(
                    color = Color(0xFF1E273A),
                    start = Offset(dpiX + 24.dp.toPx(), yMid),
                    end = Offset(serverX - 24.dp.toPx(), yMid),
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
                
                // Draw Active Interactive Flows depending on Steps
                if (selectedMethodTab == 0) { // Windows Wrong TCP Sequence Diagram
                    when (stepIndex) {
                        1 -> { // SYN Outward
                            drawArrow(clientX + 24.dp.toPx(), dpiX - 24.dp.toPx(), yMid, Color(0xFF00FFCC), this)
                        }
                        2 -> { // Decoy inject
                            // Out of bounds arrow going to DPI, but shorted or greyed from DPI to server
                            drawArrow(clientX + 24.dp.toPx(), dpiX - 5.dp.toPx(), yMid - 15.dp.toPx(), Color(0xFFFFEB3B), this)
                            // server drop
                            drawCircle(color = Color(0xFFFF3366), radius = 5.dp.toPx(), center = Offset(serverX, yMid - 15.dp.toPx()))
                        }
                        3 -> { // Real client hello slips through
                            drawArrow(clientX + 24.dp.toPx(), dpiX - 24.dp.toPx(), yMid, Color(0xFF9CCC65), this)
                            drawArrow(dpiX + 24.dp.toPx(), serverX - 24.dp.toPx(), yMid, Color(0xFF9CCC65), this)
                        }
                        4 -> { // Established communication channels
                            drawArrow(clientX + 24.dp.toPx(), serverX - 24.dp.toPx(), yMid, Color(0xFF00FFCC), this)
                            drawArrow(serverX - 24.dp.toPx(), clientX + 24.dp.toPx(), yMid + 15.dp.toPx(), Color(0xFF00E6FF), this)
                        }
                    }
                } else { // Android User Space Splitting
                    when (stepIndex) {
                        1 -> { // Slicing flight A
                            drawArrow(clientX + 24.dp.toPx(), dpiX - 50.dp.toPx(), yMid, Color(0xFFFF9800), this)
                        }
                        2 -> { // Delay state & flight B dispatch
                            drawArrow(clientX + 24.dp.toPx(), dpiX - 24.dp.toPx(), yMid, Color(0xFFFFEB3B), this)
                        }
                        3 -> { // Complete pass through reassembly
                            drawArrow(dpiX + 24.dp.toPx(), serverX - 24.dp.toPx(), yMid, Color(0xFF00FFCC), this)
                        }
                    }
                }
            }

            // Simple labels inside container
            LabelNode("CLIENT", Alignment.CenterStart, Modifier.padding(start = 12.dp))
            LabelNode("FIREWALL (DPI)", Alignment.Center, Modifier.offset(y = (-55).dp))
            LabelNode("TARGET SERVER", Alignment.CenterEnd, Modifier.padding(end = 12.dp))

            // Step Descriptive balloons at the bottom of the simulator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF090E16))
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "STEP $stepIndex: ${getBypassStepTitle(selectedMethodTab, stepIndex)}",
                        color = Color(0xFF00FFCC),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = getBypassStepDescription(selectedMethodTab, stepIndex),
                        color = Color(0xFFC7D3E2),
                        fontSize = 11.sp,
                        minLines = 3,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Flow control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (stepIndex > 1) stepIndex-- },
                enabled = stepIndex > 1,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF131B2A), contentColor = Color.White),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Previous Step", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            val totalSteps = if (selectedMethodTab == 0) 4 else 3
            Text(
                "Step $stepIndex of $totalSteps",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { if (stepIndex < totalSteps) stepIndex++ },
                enabled = stepIndex < totalSteps,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF131B2A), contentColor = Color.White),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Next Step", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Helper Composable to label system nodes
@Composable
fun BoxScope.LabelNode(name: String, alignment: Alignment, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .align(alignment)
            .background(Color(0xFF1A2234), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF2B3750), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
            .offset(y = 35.dp)
    ) {
        Text(name, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
    }
}

// Helper to draw clean visual arrows in simulation canvases
private fun drawArrow(startX: Float, endX: Float, y: Float, color: Color, drawScope: androidx.compose.ui.graphics.drawscope.DrawScope) {
    drawScope.drawLine(
        color = color,
        start = Offset(startX, y),
        end = Offset(endX, y),
        strokeWidth = 6f
    )
    // Draw pointer head
    val isRight = endX > startX
    val factor = if (isRight) 1f else -1f
    drawScope.drawLine(
        color = color,
        start = Offset(endX, y),
        end = Offset(endX - (10f * factor), y - 10f),
        strokeWidth = 6f
    )
    drawScope.drawLine(
        color = color,
        start = Offset(endX, y),
        end = Offset(endX - (10f * factor), y + 10f),
        strokeWidth = 6f
    )
}

private fun getBypassStepTitle(tab: Int, step: Int): String {
    if (tab == 0) {
        return when (step) {
            1 -> "TCP CONNECTION HANDSHAKE (SYN)"
            2 -> "INJECT DECOY PACKET (WRONG SEQ)"
            3 -> "GENUINE RAW PACKET ESCAPES"
            4 -> "STABLE FILTER-FREE DATA CHANNEL"
            else -> ""
        }
    } else {
        return when (step) {
            1 -> "DETECTION & DISPATCH PARTITION"
            2 -> "SEGMENT B FLY-BY WITH SLIGHT WAIT"
            3 -> "TCP STREAM COMPILATION AT REMOTE"
            else -> ""
        }
    }
}

private fun getBypassStepDescription(tab: Int, step: Int): String {
    if (tab == 0) {
        return when (step) {
            1 -> "A standard connection handshake starts by transmitting TCP SYN/ACK segments to the remote host. The deep-packet-inspection middlebox is keeping a watchful eye, waiting for the SSL/TLS ClientHello structure containing the Server Name (SNI) string in the payload."
            2 -> "Our active proxy (which is WinDivert on Windows) injects a fake Handshake containing safe SNI strings (e.g. mci.ir) but with a falsified, out-of-order/discrepant TCP sequence number. The passive DPI firewall analyzes it, parses the fake Whitelists, and logs the stream as accepted. The target OS kernel identifies the falsified sequence and drops the payload instantly."
            3 -> "Right after, the genuine ClientHello containing your blocked destination host (e.g. your proxy target) is transmitted using the true TCP sequence number. The DPI box, which has stored the stream state as 'approved' or whitelisted from the earlier decoy, passes it through without further inspection!"
            4 -> "Full SSL/TLS handshakes are completed successfully between user space socket buffers and remote processes. Blocked sites can be resolved stably because the middlebox DPI has been safely spoofed."
            else -> ""
        }
    } else {
        return when (step) {
            1 -> "On mobile devices without root/admin privileges, we use user-space TCP Handshake Splitting. When a client initiates a TLS connection, we detect the TLS ClientHello header byte markers ([0x16, 0x03, 0x03...]). We write only the first 3 bytes to the target, deliberately delaying the rest."
            2 -> "We pause the pipe write operations briefly (~35ms). This alerts the kernel to physically flush compile buffers and send segment A as a stand-alone TCP packet. Because standard firewall boxes require whole headers within single TCP segments to find SNI patterns, they skip scanning this incomplete packet."
            3 -> "We transmit the rest of the ClientHello containing the SNI strings in TCP segment B. The DPI middlebox overlooks it since signature flags were broken across fragment boundaries. The target server's kernel TCP buffer correctly compiles segments A and B back into a valid ClientHello frame, completing the TLS link securely!"
            else -> ""
        }
    }
}
