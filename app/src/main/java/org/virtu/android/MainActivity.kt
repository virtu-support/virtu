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
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    FloatingBottomBar(
                        selectedAction = "Start",
                        onActionClick = { action ->
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
        val intent = Intent(this, VmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopVm() {
        stopService(Intent(this, VmService::class.java))
    }

    private fun testJni() {
        val engine = VmEngine()
        engine.runCommand("uname -a")
    }
}
