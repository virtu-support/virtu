package org.virtu.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    private var selectedAction = "Start"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge and set status/nav bar colors to match dynamic background
        enableEdgeToEdge()
        // Note: enableEdgeToEdge() sets the bars to transparent, but we want them to match the background
        // We'll set them manually using the dynamic color after the content is set.

        setContent {
            MaterialTheme {
                // This Surface will use the dynamic background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Apply navigation bar padding so the floating bar doesn't overlap with system buttons
                    androidx.compose.foundation.layout.Box(
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

        // After the content is set, update the status bar and navigation bar colors
        window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()
        window.navigationBarColor = MaterialTheme.colorScheme.background.toArgb()

        // Make status bar icons light/dark according to the background brightness
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        // If the background is light, set icons to dark; else light.
        // We'll use the luminance of the background color to decide.
        val background = MaterialTheme.colorScheme.background
        val luminance = background.red * 0.299 + background.green * 0.587 + background.blue * 0.114
        insetsController.isAppearanceLightStatusBars = luminance > 0.5
        insetsController.isAppearanceLightNavigationBars = luminance > 0.5
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
