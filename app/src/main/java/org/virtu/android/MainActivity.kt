package org.virtu.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    private var selectedAction = "Start"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
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
                        },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }

        hideSystemBars()
    }

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
