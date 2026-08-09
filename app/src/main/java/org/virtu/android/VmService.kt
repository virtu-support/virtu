package org.virtu.android

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.File

class VmService : Service() {

    private val CHANNEL_ID = "VirtuChannel"
    private val NOTIFICATION_ID = 1
    private var vmProcess: Process? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVmEngine()
        return START_STICKY
    }

    private fun startVmEngine() {
        try {
            val scriptFile = File(filesDir, "install.sh")
            assets.open("install.sh").use { input ->
                scriptFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            scriptFile.setExecutable(true)

            val processBuilder = ProcessBuilder(
                "sh", "-c", "echo 'Virtu VM started!' && uname -a"
            )
            vmProcess = processBuilder.start()
            vmProcess?.inputStream?.bufferedReader()?.use {
                it.lineSequence().forEach { line ->
                    // Log output
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        vmProcess?.destroy()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Virtu Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("virtu")
            .setContentText("VM is running")
            .setSmallIcon(R.drawable.ic_launcher)
            .build()
    }
}
