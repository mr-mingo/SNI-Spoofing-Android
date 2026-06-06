package com.example.proxy

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.example.MainActivity

@RequiresApi(Build.VERSION_CODES.N)
class ProxyTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        val isRunning = ProxyManager.isRunning.value
        
        if (isRunning) {
            tile.state = Tile.STATE_ACTIVE
            tile.label = "SNI Spoofing"
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.label = "SNI Spoofing"
        }
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val isRunning = ProxyManager.isRunning.value
        val intent = Intent(this, ProxyService::class.java)

        if (isRunning) {
            intent.action = ProxyService.ACTION_STOP
            startService(intent)
        } else {
            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val listenPortStr = prefs.getString("listenPort", "40443") ?: "40443"
            val connectHost = prefs.getString("connectHost", "188.114.98.0") ?: "188.114.98.0"
            val connectPortStr = prefs.getString("connectPort", "443") ?: "443"
            val fakeSni = prefs.getString("fakeSni", "auth.vercel.com") ?: "auth.vercel.com"
            val customSni = prefs.getString("customSni", "") ?: ""
            val bypassMethodStr = prefs.getString("bypassMethod", "TCP_FRAGMENTATION") ?: "TCP_FRAGMENTATION"
            
            val activeSni = if (fakeSni == "Custom") customSni else fakeSni

            intent.action = ProxyService.ACTION_START
            intent.putExtra(ProxyService.KEY_LISTEN_PORT, listenPortStr.toIntOrNull() ?: 40443)
            intent.putExtra(ProxyService.KEY_CONNECT_HOST, connectHost)
            intent.putExtra(ProxyService.KEY_CONNECT_PORT, connectPortStr.toIntOrNull() ?: 443)
            intent.putExtra(ProxyService.KEY_FAKE_SNI, activeSni)
            intent.putExtra(ProxyService.KEY_STRATEGY, bypassMethodStr)
            
            val lang = prefs.getString("lang", "System") ?: "System"
            val isFarsi = if (lang == "System") java.util.Locale.getDefault().language == "fa" else lang == "Persian"
            
            val title = if (isFarsi) "دور زدن کلاینت فعال است" else "SNI Spoofing is Active"
            val text = if (isFarsi) "تونل پروکسی لوکال در حال اجراست." else "Local proxy tunnel is running."
            val stop = if (isFarsi) "توقف پروکسی" else "STOP SPOOFING"
            
            intent.putExtra(ProxyService.KEY_NOTIF_TITLE, title)
            intent.putExtra(ProxyService.KEY_NOTIF_TEXT, text)
            intent.putExtra(ProxyService.KEY_NOTIF_STOP, stop)
            
            startForegroundServiceCompatibility(intent)
        }
        
        // Optimistically update tile
        qsTile?.state = if (isRunning) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE
        qsTile?.updateTile()
    }
    
    private fun startForegroundServiceCompatibility(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
