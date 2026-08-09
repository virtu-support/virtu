package org.virtu.android

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Root container
        val root = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.WHITE)
        }

        // Status text (centered)
        statusText = TextView(this).apply {
            text = "Ready"
            textSize = 18f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }
        root.addView(statusText)

        // Floating bar
        val floatingBar = FloatingBar(this).apply {
            startButton.setOnClickListener { startVm() }
            stopButton.setOnClickListener { stopVm() }
            testButton.setOnClickListener { testJni() }
        }
        root.addView(floatingBar)

        setContentView(root)
    }

    private fun startVm() {
        statusText.text = "Starting VM..."
        val intent = Intent(this, VmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        statusText.text = "VM running"
    }

    private fun stopVm() {
        statusText.text = "Stopping VM..."
        stopService(Intent(this, VmService::class.java))
        statusText.text = "VM stopped"
    }

    private fun testJni() {
        val engine = VmEngine()
        val result = engine.runCommand("uname -a")
        statusText.text = "JNI Test: $result"
    }
}
