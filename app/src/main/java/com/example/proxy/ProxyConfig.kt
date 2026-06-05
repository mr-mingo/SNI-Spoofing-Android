package com.example.proxy

data class ProxyConfig(
    val listenHost: String = "127.0.0.1",
    val listenPort: Int = 40443,
    val connectHost: String = "188.114.98.0", // Clean IP/Node
    val connectPort: Int = 443,
    val fakeSni: String = "auth.vercel.com",
    val bypassMethod: String = "TCP_FRAGMENTATION" // "TCP_FRAGMENTATION" or "DIRECT_TUNNEL"
)
