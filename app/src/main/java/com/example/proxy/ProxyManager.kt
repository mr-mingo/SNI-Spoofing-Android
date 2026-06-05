package com.example.proxy

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ProxyManager {
    val isRunning = MutableStateFlow(false)
    val txBytes = MutableStateFlow(0L)
    val rxBytes = MutableStateFlow(0L)
    val activeConnections = MutableStateFlow(0)
    val logs = mutableStateListOf<String>()

    private val scope = CoroutineScope(Dispatchers.Main)

    fun addLog(message: String) {
        scope.launch {
            val timeStamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            if (logs.size > 200) {
                logs.removeAt(0)
            }
            logs.add("[$timeStamp] $message")
        }
    }

    fun clearLogs() {
        scope.launch {
            logs.clear()
        }
    }
}
