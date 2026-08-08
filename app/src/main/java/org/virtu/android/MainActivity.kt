package org.virtu.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        startButton.setOnClickListener {
            startVm()
        }
        stopButton.setOnClickListener {
            stopVm()
        }

        // Test JNI
        val testButton: Button = findViewById(R.id.testButton)
        testButton.setOnClickListener {
            val engine = VmEngine()
            val result = engine.runCommand("uname -a")
            statusText.text = "Result: $result"
        }
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
}
