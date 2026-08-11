package org.virtu.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private var selectedTab by mutableStateOf(0)  // 0: Home, 1: Distros, 2: Terminal, 3: Tools, 4: Settings
    private var statusText by mutableStateOf("Ready")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                val backgroundColor = MaterialTheme.colorScheme.background
                val view = LocalView.current
                SideEffect {
                    val window = (view.context as ComponentActivity).window
                    window.statusBarColor = backgroundColor.toArgb()
                    window.navigationBarColor = backgroundColor.toArgb()
                    val insetsController = WindowCompat.getInsetsController(window, view)
                    val luminance = backgroundColor.red * 0.299 + backgroundColor.green * 0.587 + backgroundColor.blue * 0.114
                    insetsController.isAppearanceLightStatusBars = luminance > 0.5
                    insetsController.isAppearanceLightNavigationBars = luminance > 0.5
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("virtu", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                if (statusText == "VM running") Color.Green else Color.Red,
                                                shape = MaterialTheme.shapes.small
                                            )
                                    )
                                    Text(
                                        text = statusText,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { /* More options later */ }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                                }
                            }
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            when (selectedTab) {
                                0 -> HomeScreen(
                                    onVmAction = { action, vmId ->
                                        when (action) {
                                            "play" -> {
                                                statusText = "Starting VM..."
                                                startVm()
                                            }
                                            "resume" -> {
                                                statusText = "Resuming VM..."
                                                startVm()
                                            }
                                            "stop" -> {
                                                statusText = "Stopping VM..."
                                                stopVm()
                                            }
                                            "freeze" -> {
                                                statusText = "Freezing VM..."
                                                // TODO: implement freeze
                                            }
                                            "settings" -> {
                                                // TODO: open per-VM settings
                                            }
                                        }
                                    }
                                )
                                1 -> DistrosContent()
                                2 -> TerminalContent()
                                3 -> ToolsScreen(
                                    onPluginInstall = { pluginId, command ->
                                        statusText = "Installing $pluginId..."
                                        runCommand(command)
                                    }
                                )
                                4 -> SettingsContent()
                            }
                        }
                    }

                    FloatingBottomBar(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }

        hideSystemBars()
    }

    // -------- TAB CONTENTS --------

    @Composable
    fun DistrosContent() {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Distros", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("List of pre‑installed and available distributions.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }

    @Composable
    fun TerminalContent() {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Terminal", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("root@localhost:~#", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            Text("(Default root shell via Termux)", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }

    @Composable
    fun SettingsContent() {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Global settings and per‑VM settings.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Version ${BuildConfig.VERSION_NAME}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }

    // -------- SYSTEM UI --------

    private fun hideSystemBars() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    // -------- VM ACTIONS --------

    private fun startVm() {
        statusText = "VM running"
        val intent = Intent(this, VmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopVm() {
        statusText = "VM stopped"
        stopService(Intent(this, VmService::class.java))
    }

    private fun testJni() {
        statusText = "Testing JNI..."
        val engine = VmEngine()
        engine.runCommand("uname -a")
        statusText = "JNI test done"
    }

    // -------- TOOLS (curl) --------

    private fun runCommand(command: String) {
        Thread {
            try {
                val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
                val exitCode = process.waitFor()
                statusText = if (exitCode == 0) "Installation successful" else "Installation failed (exit $exitCode)"
            } catch (e: Exception) {
                statusText = "Error: ${e.message}"
            }
        }.start()
    }
}

// Inside the class, add this function for loading config if not using VmConfig
// but we already have VmConfig. We'll use it.

@Composable
fun TerminalContent() {
    val isVMRunning by remember { mutableStateOf(statusText == "VM running") }
    val vncPort = remember { mutableIntStateOf(5900) }

    // Load port from config
    LaunchedEffect(Unit) {
        val config = VmConfig.load()
        vncPort.intValue = config.getProperty("VNC_PORT", "5900").toInt()
    }

    if (isVMRunning) {
        DisplayScreen(
            host = "127.0.0.1",
            port = vncPort.intValue,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Terminal / Display", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Start a VM to see the graphical display here.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}
