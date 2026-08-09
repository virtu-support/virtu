package org.virtu.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    private var selectedAction = "Start"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary // dynamic primary color
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
