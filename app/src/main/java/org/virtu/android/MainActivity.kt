package org.virtu.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    private var selectedAction = "Start"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        enableEdgeToEdge()

        setContent {
            // Use dynamic background color from Material You
            val backgroundColor = MaterialTheme.colorScheme.background

            // Update status bar and navigation bar colors to match the background
            val view = LocalView.current
            SideEffect {
                val window = (view.context as ComponentActivity).window
                window.statusBarColor = backgroundColor.toArgb()
                window.navigationBarColor = backgroundColor.toArgb()

                // Set light/dark icons based on background luminance
                val insetsController = WindowCompat.getInsetsController(window, view)
                val luminance = backgroundColor.red * 0.299 + backgroundColor.green * 0.587 + backgroundColor.blue * 0.114
                insetsController.isAppearanceLightStatusBars = luminance > 0.5
                insetsController.isAppearanceLightNavigationBars = luminance > 0.5
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = backgroundColor
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    FloatingBottomBar(
                        selectedAction = selectedAction,
                        onActionClick = { action ->
                            selectedAction = action
                            when (action) {
                                "Start" -> startVm()
                                "Stop" -> stopVm()
                                "Test" -> testJni()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun startVm() {
        selectedAction = "Start"
        val intent = Intent(this, VmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopVm() {
        selectedAction = "Stop"
        stopService(Intent(this, VmService::class.java))
    }

    private fun testJni() {
        selectedAction = "Test"
        val engine = VmEngine()
        engine.runCommand("uname -a")
    }
}
