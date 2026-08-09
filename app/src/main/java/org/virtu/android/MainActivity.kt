package org.virtu.android

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var floatingBar: FloatingBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.WHITE)
        }

        floatingBar = FloatingBottomBar(this).apply {
            onStartClick = { startVm() }
            onStopClick = { stopVm() }
            onTestClick = { testJni() }

            // Optionally set initial selected state
            setSelected("Start") // or "Stop" / "Test"
        }
        root.addView(floatingBar)

        setContentView(root)
    }

    private fun startVm() {
        floatingBar.setSelected("Start")
        floatingBar.statusText.text = "Starting VM..."
        val intent = Intent(this, VmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        floatingBar.statusText.text = "VM running"
    }

    private fun stopVm() {
        floatingBar.setSelected("Stop")
        floatingBar.statusText.text = "Stopping VM..."
        stopService(Intent(this, VmService::class.java))
        floatingBar.statusText.text = "VM stopped"
    }

    private fun testJni() {
        floatingBar.setSelected("Test")
        val engine = VmEngine()
        val result = engine.runCommand("uname -a")
        floatingBar.statusText.text = "JNI Test: $result"
    }
}
