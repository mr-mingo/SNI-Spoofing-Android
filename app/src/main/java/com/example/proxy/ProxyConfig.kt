package com.example.proxy

data class ProxyConfig(
    val listenHost: String = "127.0.0.1",
    val listenPort: Int = 8080,
    val connectHost: String = "104.21.32.203", // Default connect node
    val connectPort: Int = 443,
    val fakeSni: String = "mci.ir",
    val bypassMethod: String = "TCP_FRAGMENTATION" // "TCP_FRAGMENTATION" or "DIRECT_TUNNEL"
)
