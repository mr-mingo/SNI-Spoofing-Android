package com.example

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.os.Build
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proxy.ProxyConfig
import com.example.proxy.ProxyManager
import com.example.proxy.ProxyService
import com.example.ui.EnglishStrings
import com.example.ui.LocalStrings
import com.example.ui.PersianStrings
import com.example.ui.theme.MyApplicationTheme
import java.util.Locale

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val prefs = remember { getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
            
            var language by remember { mutableStateOf(prefs.getString("lang", "System") ?: "System") }
            var themePref by remember { mutableStateOf(prefs.getString("theme", "System") ?: "System") }

            val systemInDarkTheme = isSystemInDarkTheme()
            val isDarkTheme = when (themePref) {
                "Dark" -> true
                "Light" -> false
                else -> systemInDarkTheme
            }

            val systemLang = Locale.getDefault().language
            val isFarsi = when (language) {
                "Fa" -> true
                "Eng" -> false
                else -> systemLang == "fa"
            }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                CompositionLocalProvider(
                    LocalStrings provides if (isFarsi) PersianStrings else EnglishStrings,
                    LocalLayoutDirection provides if (isFarsi) LayoutDirection.Rtl else LayoutDirection.Ltr
                ) {
                    val bgColor = if (isDarkTheme) Color(0xFF07090E) else Color(0xFFF0F4F8)
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = bgColor
                    ) {
                        AppRootScreen(
                            onLanguageChange = {
                                language = it
                                prefs.edit().putString("lang", it).apply()
                            },
                            onThemeChange = {
                                themePref = it
                                prefs.edit().putString("theme", it).apply()
                            },
                            language = language,
                            themePref = themePref,
                            prefs = prefs
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppRootScreen(
    onLanguageChange: (String) -> Unit,
    onThemeChange: (String) -> Unit,
    language: String,
    themePref: String,
    prefs: android.content.SharedPreferences
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val context = LocalContext.current
    val strings = LocalStrings.current
    var selectedTab by remember { mutableStateOf(0) }
    
    val isProxyRunning by ProxyManager.isRunning.collectAsStateWithLifecycle()
    val txBytes by ProxyManager.txBytes.collectAsStateWithLifecycle()
    val rxBytes by ProxyManager.rxBytes.collectAsStateWithLifecycle()
    val activeConnections by ProxyManager.activeConnections.collectAsStateWithLifecycle()
    val logs = ProxyManager.logs

    // State parameters held
    var listenPort by remember { mutableStateOf(prefs.getString("listenPort", "40443") ?: "40443") }
    var connectHost by remember { mutableStateOf(prefs.getString("connectHost", "188.114.98.0") ?: "188.114.98.0") }
    var connectPort by remember { mutableStateOf(prefs.getString("connectPort", "443") ?: "443") }
    var fakeSni by remember { mutableStateOf(prefs.getString("fakeSni", "auth.vercel.com") ?: "auth.vercel.com") }
    var customSni by remember { mutableStateOf(prefs.getString("customSni", "") ?: "") }
    var bypassMethod by remember { mutableStateOf(prefs.getString("bypassMethod", "TCP_FRAGMENTATION") ?: "TCP_FRAGMENTATION") }
    
    var savedListenPort by remember { mutableStateOf(listenPort) }
    var savedConnectHost by remember { mutableStateOf(connectHost) }
    var savedConnectPort by remember { mutableStateOf(connectPort) }
    var savedFakeSni by remember { mutableStateOf(fakeSni) }
    var savedCustomSni by remember { mutableStateOf(customSni) }
    var savedBypassMethod by remember { mutableStateOf(bypassMethod) }

    val systemInDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    val isDark = if (themePref == "System") systemInDarkTheme else themePref == "Dark"
    
    val navColor = if (isDark) Color(0xFF0D111A) else Color(0xFFFFFFFF)
    val navBorderColor = if (isDark) Color(0xFF1E2638) else Color(0xFFD3DFEB)
    
    val unselectedIconColor = if (isDark) Color(0xFF8D9CB0) else Color(0xFF90A4AE)

    val hasUnsavedChanges = listenPort != savedListenPort ||
            connectHost != savedConnectHost ||
            connectPort != savedConnectPort ||
            fakeSni != savedFakeSni ||
            customSni != savedCustomSni ||
            bypassMethod != savedBypassMethod

    val activeConfig = remember(listenPort, connectHost, connectPort, fakeSni, customSni, bypassMethod) {
        ProxyConfig(
            listenHost = "127.0.0.1",
            listenPort = listenPort.toIntOrNull() ?: 40443,
            connectHost = connectHost.trim(),
            connectPort = connectPort.toIntOrNull() ?: 443,
            fakeSni = if (fakeSni == "Custom ...") customSni.trim() else fakeSni.trim(),
            bypassMethod = bypassMethod
        )
    }

    val savePrefs = {
        prefs.edit().apply {
            putString("listenPort", listenPort)
            putString("connectHost", connectHost)
            putString("connectPort", connectPort)
            putString("fakeSni", fakeSni)
            putString("customSni", customSni)
            putString("bypassMethod", bypassMethod)
            apply()
        }
        savedListenPort = listenPort
        savedConnectHost = connectHost
        savedConnectPort = connectPort
        savedFakeSni = fakeSni
        savedCustomSni = customSni
        savedBypassMethod = bypassMethod
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = navColor,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .border(1.dp, navBorderColor, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = strings.dashboard) },
                    label = { Text(strings.dashboard, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = unselectedIconColor,
                        unselectedTextColor = unselectedIconColor,
                        indicatorColor = if (isDark) Color(0xFF131E31) else Color(0xFFE0F2F1)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = strings.logs) },
                    label = { Text(strings.logs, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = unselectedIconColor,
                        unselectedTextColor = unselectedIconColor,
                        indicatorColor = if (isDark) Color(0xFF131E31) else Color(0xFFE0F2F1)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = strings.settings) },
                    label = { Text(strings.settings, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = unselectedIconColor,
                        unselectedTextColor = unselectedIconColor,
                        indicatorColor = if (isDark) Color(0xFF131E31) else Color(0xFFE0F2F1)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Info, contentDescription = strings.about) },
                    label = { Text(strings.about, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BFA5),
                        selectedTextColor = Color(0xFF00BFA5),
                        unselectedIconColor = unselectedIconColor,
                        unselectedTextColor = unselectedIconColor,
                        indicatorColor = if (isDark) Color(0xFF131E31) else Color(0xFFE0F2F1)
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
            // Header bar
            val headerBg = if (isDark) Color(0xFF0C0F16) else Color(0xFFFFFFFF)
            val headerBorder = if (isDark) Color(0xFF181F2F) else Color(0xFFE2E8F0)
            val textColor = if (isDark) Color.White else Color(0xFF1A202C)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBg)
                    .border(width = 1.dp, color = headerBorder)
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
                                strings.appName,
                                letterSpacing = 2.sp,
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    // Power Toggle Button in Header
                    Button(
                        onClick = { 
                            if (isProxyRunning) {
                                val intent = Intent(context, ProxyService::class.java).apply {
                                    action = ProxyService.ACTION_STOP
                                }
                                context.startService(intent)
                            } else if (hasUnsavedChanges) {
                                savePrefs()
                            } else {
                                savePrefs()
                                val intent = Intent(context, ProxyService::class.java).apply {
                                    action = ProxyService.ACTION_START
                                    putExtra(ProxyService.KEY_LISTEN_PORT, activeConfig.listenPort)
                                    putExtra(ProxyService.KEY_CONNECT_HOST, activeConfig.connectHost)
                                    putExtra(ProxyService.KEY_CONNECT_PORT, activeConfig.connectPort)
                                    putExtra(ProxyService.KEY_FAKE_SNI, activeConfig.fakeSni)
                                    putExtra(ProxyService.KEY_STRATEGY, activeConfig.bypassMethod)
                                    putExtra(ProxyService.KEY_NOTIF_TITLE, strings.notificationTitle)
                                    putExtra(ProxyService.KEY_NOTIF_TEXT, strings.notificationText)
                                    putExtra(ProxyService.KEY_NOTIF_STOP, strings.stop)
                                }
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    context.startForegroundService(intent)
                                } else {
                                    context.startService(intent)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isProxyRunning) Color(0xFFFF2E56) 
                                             else if (hasUnsavedChanges) Color(0xFF4CAF50)
                                             else Color(0xFF00E6FF),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (isProxyRunning) Icons.Default.Close 
                                          else if (hasUnsavedChanges) Icons.Default.Check
                                          else Icons.Default.PlayArrow,
                            contentDescription = if (isProxyRunning) strings.stop 
                                                 else if (hasUnsavedChanges) strings.saveChanges
                                                 else strings.start,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isProxyRunning) strings.stop 
                                   else if (hasUnsavedChanges) strings.saveChanges
                                   else strings.start,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                    },
                    label = "TabTransition"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> DashboardScreen(activeConnections, txBytes, rxBytes, activeConfig, isDark)
                        1 -> LogsScreen(logs, isDark)
                        2 -> SettingsScreen(
                            listenPort = listenPort,
                            onListenPortChange = { listenPort = it },
                            connectHost = connectHost,
                            onConnectHostChange = { connectHost = it },
                            connectPort = connectPort,
                            onConnectPortChange = { connectPort = it },
                            fakeSni = fakeSni,
                            onFakeSniChange = { fakeSni = it },
                            customSni = customSni,
                            onCustomSniChange = { customSni = it },
                            bypassMethod = bypassMethod,
                            onBypassMethodChange = { bypassMethod = it },
                            isProxyRunning = isProxyRunning,
                            isDarkTheme = isDark,
                            themePref = themePref,
                            onThemeChange = onThemeChange,
                            language = language,
                            onLanguageChange = onLanguageChange
                        )
                        3 -> AboutScreen(isDark)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    activeConnections: Int,
    txBytes: Long,
    rxBytes: Long,
    activeConfig: ProxyConfig,
    isDark: Boolean
) {
    val strings = LocalStrings.current
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val cardBg = if (isDark) Color(0xFF0F1420) else Color(0xFFFFFFFF)
    val cardBorder = if (isDark) Color(0xFF1B2236) else Color(0xFFE2E8F0)
    val textColor = if (isDark) Color.White else Color(0xFF1E293B)
    val subTextColor = if (isDark) Color(0xFF8F9FBC) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Network Performance Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f).height(100.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF00BFA5), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.txTitle, color = subTextColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = bytesToSymbolicString(txBytes),
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f).height(100.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFF00BFA5), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.rxTitle, color = subTextColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = bytesToSymbolicString(rxBytes),
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Summary Block
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.dp, cardBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(strings.activeFlows, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = subTextColor, letterSpacing = 1.sp)
                    Text(
                        "$activeConnections", 
                        color = if (activeConnections > 0) Color(0xFF00BFA5) else textColor,
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
                
                HorizontalDivider(
                    modifier = Modifier.height(40.dp).width(1.dp),
                    color = cardBorder
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(strings.targetAddress, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = subTextColor, letterSpacing = 1.sp)
                    Text(
                        "${activeConfig.connectHost}:${activeConfig.connectPort}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        // Android proxy routing tip
        val tipBgColor = if (isDark) Color(0xFF0E131F) else Color(0xFFE0F2FE)
        val tipBorderColor = if (isDark) Color(0xFF1E263B) else Color(0xFFBCE3FF)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(tipBgColor, RoundedCornerShape(12.dp))
                .border(1.dp, tipBorderColor, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    strings.connectionTipTitle,
                    color = if (isDark) Color(0xFF00FFCC) else Color(0xFF0284C7),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                strings.connectionTipBody,
                color = if (isDark) Color(0xFFC5D2E5) else Color(0xFF0F172A),
                lineHeight = 22.sp,
                fontSize = 13.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDark) Color(0xFF2E1065) else Color(0xFFEDE9FE))
                        .border(1.dp, if (isDark) Color(0xFF4C1D95) else Color(0xFFDDD6FE), RoundedCornerShape(8.dp))
                        .clickable {
                            clipboardManager.setText(AnnotatedString("127.0.0.1"))
                            Toast.makeText(context, "Host copied!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Host", fontSize = 11.sp, color = if(isDark) Color(0xFFA78BFA) else Color(0xFF7C3AED), fontWeight = FontWeight.Bold)
                        Text("127.0.0.1", fontSize = 14.sp, color = if(isDark) Color.White else Color(0xFF4C1D95), fontWeight = FontWeight.Black)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDark) Color(0xFF064E3B) else Color(0xFFD1FAE5))
                        .border(1.dp, if (isDark) Color(0xFF047857) else Color(0xFFA7F3D0), RoundedCornerShape(8.dp))
                        .clickable {
                            clipboardManager.setText(AnnotatedString(activeConfig.listenPort.toString()))
                            Toast.makeText(context, "Port copied!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Port", fontSize = 11.sp, color = if(isDark) Color(0xFF34D399) else Color(0xFF059669), fontWeight = FontWeight.Bold)
                        Text("${activeConfig.listenPort}", fontSize = 14.sp, color = if(isDark) Color.White else Color(0xFF064E3B), fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString("127.0.0.1:${activeConfig.listenPort}"))
                    Toast.makeText(context, strings.logsCopiedMsg, Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color(0xFF1E2F4C) else Color(0xFFBAE6FD),
                    contentColor = if (isDark) Color(0xFF00FFCC) else Color(0xFF0369A1)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(strings.copyProxyBtn, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LogsScreen(logs: List<String>, isDark: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val strings = LocalStrings.current

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            scrollState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFF00BFA5), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    strings.terminalTitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) Color(0xFF8D9CB0) else Color(0xFF64748B),
                    letterSpacing = 1.sp
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    strings.copyLogs,
                    color = Color(0xFF00BFA5),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(logs.joinToString("\n")))
                        Toast.makeText(context, strings.logsCopiedMsg, Toast.LENGTH_SHORT).show()
                    }.padding(4.dp)
                )
                Text(
                    strings.clearLogs,
                    color = Color(0xFFFF5271),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.clickable {
                        ProxyManager.clearLogs()
                        Toast.makeText(context, strings.logsClearedMsg, Toast.LENGTH_SHORT).show()
                    }.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF040609) else Color(0xFF1E293B)),
            border = BorderStroke(1.dp, if (isDark) Color(0xFF1F293D) else Color(0xFF334155)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                if (logs.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            strings.terminalIdleTitle,
                            color = Color(0xFF64748B),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            strings.terminalIdleSubtitle,
                            color = Color(0xFF64748B),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
                        items(logs) { log ->
                            Text(
                                text = log,
                                color = getLogColor(log),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    customSni: String,
    onCustomSniChange: (String) -> Unit,
    bypassMethod: String,
    onBypassMethodChange: (String) -> Unit,
    isProxyRunning: Boolean,
    isDarkTheme: Boolean,
    themePref: String,
    onThemeChange: (String) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit
) {
    val strings = LocalStrings.current
    val textColor = if (isDarkTheme) Color.White else Color(0xFF1E293B)
    val subColor = if (isDarkTheme) Color(0xFF8A98B0) else Color(0xFF64748B)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(strings.settingsTitle, fontWeight = FontWeight.Black, color = textColor, fontSize = 16.sp)
            Text(strings.settingsSubtitle, color = subColor, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                strings.appPreferencesTitle,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFF8DABDF) else Color(0xFF0284C7),
                fontSize = 13.sp
            )
        }

        item {
            var expanded by remember { mutableStateOf(false) }
            val options = listOf("System", "Eng", "Fa")
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = if (language == "Fa") "فارسی" else if(language == "Eng") "English" else strings.systemDefault,
                    onValueChange = {},
                    label = { Text(strings.languageLabel) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = textFieldColors(isDarkTheme),
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(if (selectionOption == "Fa") "فارسی" else if(selectionOption == "Eng") "English" else strings.systemDefault) },
                            onClick = {
                                onLanguageChange(selectionOption)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            var expanded by remember { mutableStateOf(false) }
            val options = listOf("System", "Dark", "Light")
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = if (themePref == "System") strings.systemDefault else themePrefToText(themePref = themePref, isFarsi = language == "Fa"),
                    onValueChange = {},
                    label = { Text(strings.themeLabel) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = textFieldColors(isDarkTheme),
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(if(selectionOption == "System") strings.systemDefault else themePrefToText(themePref = selectionOption, isFarsi = language == "Fa")) },
                            onClick = {
                                onThemeChange(selectionOption)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        
        item {
            HorizontalDivider(color = subColor.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
        }

        item {
            Text(
                strings.proxyConfigTitle,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFF8DABDF) else Color(0xFF0284C7),
                fontSize = 13.sp
            )
        }

        item {
            OutlinedTextField(
                value = listenPort,
                onValueChange = onListenPortChange,
                enabled = !isProxyRunning,
                label = { Text(strings.listenPortLabel) },
                colors = textFieldColors(isDarkTheme),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = connectHost,
                onValueChange = onConnectHostChange,
                enabled = !isProxyRunning,
                label = { Text(strings.connectHostLabel) },
                colors = textFieldColors(isDarkTheme),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = connectPort,
                onValueChange = onConnectPortChange,
                enabled = !isProxyRunning,
                label = { Text(strings.connectPortLabel) },
                colors = textFieldColors(isDarkTheme),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        item {
            var expanded by remember { mutableStateOf(false) }
            val sniPresets = listOf(
                "auth.vercel.com", "www.canva.com", "cdn.jsdelivr.net", "update.microsoft.com", 
                "www.mozilla.org", "www.asus.com", "www.lenovo.com", "dl.google.com", 
                "www.dell.com", "www.hp.com", "Custom ..."
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { if (!isProxyRunning) expanded = !expanded },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = fakeSni,
                    onValueChange = {},
                    enabled = !isProxyRunning,
                    label = { Text(strings.sniSelectionLabel) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = textFieldColors(isDarkTheme),
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    sniPresets.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                onFakeSniChange(selectionOption)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        if (fakeSni == "Custom ...") {
            item {
                OutlinedTextField(
                    value = customSni,
                    onValueChange = onCustomSniChange,
                    enabled = !isProxyRunning,
                    label = { Text(strings.customSniLabel) },
                    colors = textFieldColors(isDarkTheme),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Bypass Strategy Picker
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                strings.bypassStrategyLabel,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFF8DABDF) else Color(0xFF0284C7),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            val boxBg = if (isDarkTheme) Color(0xFF0D121B) else Color(0xFFFFFFFF)
            val boxBorder = if (isDarkTheme) Color(0xFF222B3D) else Color(0xFFCBD5E1)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(boxBg, RoundedCornerShape(8.dp))
                    .border(1.dp, boxBorder, RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isProxyRunning) { onBypassMethodChange("TCP_FRAGMENTATION") }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = bypassMethod == "TCP_FRAGMENTATION",
                        onClick = { onBypassMethodChange("TCP_FRAGMENTATION") },
                        enabled = !isProxyRunning,
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00BFA5))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(strings.strategyTcpTitle, color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(strings.strategyTcpDesc, color = subColor, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
                
                HorizontalDivider(color = boxBorder)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isProxyRunning) { onBypassMethodChange("DIRECT_TUNNEL") }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = bypassMethod == "DIRECT_TUNNEL",
                        onClick = { onBypassMethodChange("DIRECT_TUNNEL") },
                        enabled = !isProxyRunning,
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00BFA5))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(strings.strategyDirectTitle, color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(strings.strategyDirectDesc, color = subColor, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}

fun themePrefToText(themePref: String, isFarsi: Boolean): String {
    return if (isFarsi) {
        if(themePref == "Dark") "تاریک (Dark)" else "روشن (Light)"
    } else {
        themePref
    }
}

@Composable
fun textFieldColors(isDark: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF00BFA5),
    unfocusedBorderColor = if (isDark) Color(0xFF26334F) else Color(0xFFCBD5E1),
    focusedLabelColor = Color(0xFF00BFA5),
    unfocusedLabelColor = if (isDark) Color(0xFF8A98B0) else Color(0xFF64748B),
    focusedTextColor = if (isDark) Color.White else Color(0xFF1E293B),
    unfocusedTextColor = if (isDark) Color.White else Color(0xFF1E293B)
)

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
fun AboutScreen(isDark: Boolean) {
    val context = LocalContext.current
    val strings = LocalStrings.current
    val uriAuthor = Uri.parse("https://github.com/mr-mingo/SNI-Spoofing-Android")
    val uriPatterniha = Uri.parse("https://github.com/patterniha/SNI-Spoofing")
    val textColor = if (isDark) Color.White else Color(0xFF1E293B)
    val subColor = if (isDark) Color(0xFF8A98B0) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Author Card
        Card(
            modifier = Modifier.fillMaxWidth().clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, uriAuthor))
            },
            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF0D121B) else Color.White),
            border = BorderStroke(1.dp, if (isDark) Color(0xFF222B3D) else Color(0xFFCBD5E1))
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    androidx.compose.ui.res.painterResource(id = R.drawable.ic_github), 
                    contentDescription = null, 
                    tint = if (isDark) Color.White else Color.Black, 
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(strings.authorGithubTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(strings.authorGithubSubTitle, fontSize = 14.sp, color = subColor, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, uriAuthor)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFA5))
                ) {
                    Text(strings.openGithubBtn, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Patterniha Card
        Card(
            modifier = Modifier.fillMaxWidth().clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, uriPatterniha))
            },
            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF0D121B) else Color.White),
            border = BorderStroke(1.dp, if (isDark) Color(0xFF222B3D) else Color(0xFFCBD5E1))
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    androidx.compose.ui.res.painterResource(id = R.drawable.ic_github), 
                    contentDescription = null, 
                    tint = if (isDark) Color.White else Color.Black, 
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(strings.poweredByTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(strings.poweredBySubTitle, fontSize = 14.sp, color = subColor, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, uriPatterniha)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488))
                ) {
                    Text(strings.openGithubBtn, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            strings.simulatorInfo,
            fontWeight = FontWeight.Black,
            color = textColor,
            fontSize = 18.sp,
            letterSpacing = 1.sp
        )
        Text(
            strings.connectionTipBody,
            color = subColor,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        // Animated State diagram with Vectors
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(if (isDark) Color(0xFF04060A) else Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                .border(2.dp, if (isDark) Color(0xFF1B2336) else Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            // Background Paths
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val yMid = height * 0.45f
                val clientX = width * 0.15f
                val dpiX = width * 0.5f
                val serverX = width * 0.85f

                drawLine(
                    color = Color(0xFF64748B),
                    start = Offset(clientX, yMid),
                    end = Offset(serverX, yMid),
                    strokeWidth = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f))
                )

                // draw lines simulating traffic flow
                drawArrow(clientX + 24.dp.toPx(), dpiX - 35.dp.toPx(), yMid - 15.dp.toPx(), Color(0xFFFF9800), this)
                drawArrow(clientX + 24.dp.toPx(), dpiX - 15.dp.toPx(), yMid, Color(0xFFFFEB3B), this)
                drawArrow(dpiX + 24.dp.toPx(), serverX - 24.dp.toPx(), yMid, Color(0xFF00FFCC), this)
            }

            // Client Component
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = 16.dp, y = (-20).dp)
                    .size(64.dp)
                    .background(Color(0xFF1E293B), CircleShape)
                    .border(2.dp, Color(0xFF475569), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Client", tint = Color.White, modifier = Modifier.size(32.dp))
            }

            // DPI Component
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-20).dp)
                    .size(64.dp)
                    .background(Color(0xFF4C0519), CircleShape)
                    .border(2.dp, Color(0xFF881F3B), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Warning, contentDescription = "DPI", tint = Color(0xFFFDA4AF), modifier = Modifier.size(32.dp))
            }

            // Server Component
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-16).dp, y = (-20).dp)
                    .size(64.dp)
                    .background(Color(0xFF064E3B), CircleShape)
                    .border(2.dp, Color(0xFF047857), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Share, contentDescription = "Server", tint = Color(0xFF6EE7B7), modifier = Modifier.size(32.dp))
            }

            // Simple labels inside container
            LabelNode("CLIENT", Alignment.CenterStart, Modifier.padding(start = 22.dp).offset(y = 35.dp))
            LabelNode("FIREWALL (DPI)", Alignment.Center, Modifier.offset(y = 35.dp))
            LabelNode("TARGET SERVER", Alignment.CenterEnd, Modifier.padding(end = 12.dp).offset(y = 35.dp))
        }
    }
}

@Composable
fun BoxScope.LabelNode(name: String, alignment: Alignment, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .align(alignment)
            .background(Color(0xFF0F172A), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Text(name, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
    }
}

private fun drawArrow(startX: Float, endX: Float, y: Float, color: Color, drawScope: androidx.compose.ui.graphics.drawscope.DrawScope) {
    drawScope.drawLine(
        color = color,
        start = Offset(startX, y),
        end = Offset(endX, y),
        strokeWidth = 6f
    )
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
