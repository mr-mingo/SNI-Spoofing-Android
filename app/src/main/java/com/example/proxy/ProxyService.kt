package com.example.proxy

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class ProxyService : Service() {
    private var proxyServer: SniProxyServer? = null

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"

        // config keys
        const val KEY_LISTEN_PORT = "KEY_LISTEN_PORT"
        const val KEY_CONNECT_HOST = "KEY_CONNECT_HOST"
        const val KEY_CONNECT_PORT = "KEY_CONNECT_PORT"
        const val KEY_FAKE_SNI = "KEY_FAKE_SNI"
        const val KEY_STRATEGY = "KEY_STRATEGY"
        const val KEY_NOTIF_TITLE = "KEY_NOTIF_TITLE"
        const val KEY_NOTIF_TEXT = "KEY_NOTIF_TEXT"
        const val KEY_NOTIF_STOP = "KEY_NOTIF_STOP"
    }

    private var notifTitle = "SNI Spoofing is Active"
    private var notifText = "Local proxy tunnel is running."
    private var notifStop = "STOP SPOOFING"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_START) {
            val listenPort = intent.getIntExtra(KEY_LISTEN_PORT, 40443)
            val connectHost = intent.getStringExtra(KEY_CONNECT_HOST) ?: "188.114.98.0"
            val connectPort = intent.getIntExtra(KEY_CONNECT_PORT, 443)
            val fakeSni = intent.getStringExtra(KEY_FAKE_SNI) ?: "auth.vercel.com"
            val bypassMethod = intent.getStringExtra(KEY_STRATEGY) ?: "TCP_FRAGMENTATION"
            notifTitle = intent.getStringExtra(KEY_NOTIF_TITLE) ?: notifTitle
            notifText = intent.getStringExtra(KEY_NOTIF_TEXT) ?: notifText
            notifStop = intent.getStringExtra(KEY_NOTIF_STOP) ?: notifStop

            val config = ProxyConfig(
                listenHost = "127.0.0.1",
                listenPort = listenPort,
                connectHost = connectHost,
                connectPort = connectPort,
                fakeSni = fakeSni,
                bypassMethod = bypassMethod
            )

            startProxy(config)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                try {
                    startForeground(1, createNotification(), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
                } catch (e: Exception) {
                    startForeground(1, createNotification())
                }
            } else {
                startForeground(1, createNotification())
            }
        } else if (intent?.action == ACTION_STOP) {
            stopProxy()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun startProxy(config: ProxyConfig) {
        if (proxyServer?.isRunning == true) return

        proxyServer = SniProxyServer(
            config = config,
            onLog = { ProxyManager.addLog(it) },
            onStatsUpdate = { tx, rx ->
                ProxyManager.txBytes.value = tx
                ProxyManager.rxBytes.value = rx
            },
            onConnectionCount = { count ->
                ProxyManager.activeConnections.value = count
            }
        )
        proxyServer?.start()
        ProxyManager.isRunning.value = true
    }

    private fun stopProxy() {
        proxyServer?.stop()
        proxyServer = null
        ProxyManager.isRunning.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopProxy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val channelId = "ProxyServiceChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SNI Proxy Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, ProxyService::class.java).apply { action = ACTION_STOP }
        val stopPendingIntent = PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(notifTitle)
            .setContentText(notifText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_delete, notifStop, stopPendingIntent)
            .setOngoing(true)
            .build()
    }
}
