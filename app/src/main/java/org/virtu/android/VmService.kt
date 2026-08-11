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
    private var vncPort = 5900

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
            val config = VmConfig.load()
            val qemuBin = config.getProperty("QEMU_BIN", "/data/data/com.termux/files/usr/bin/qemu-system-x86_64")
            val diskImage = config.getProperty("DISK_PATH", "/storage/emulated/0/virtu/distros/ubuntu/ubuntu.img")
            val ram = config.getProperty("RAM", "1024")
            val cpu = config.getProperty("CPU", "2")
            vncPort = config.getProperty("VNC_PORT", "5900").toInt()

            // Ensure the binary is executable
            val qemuFile = File(qemuBin)
            if (!qemuFile.exists()) {
                // Try using the bundled binary (if you bundle it)
                // You can copy from assets to internal storage here
                return
            }
            if (!qemuFile.canExecute()) {
                qemuFile.setExecutable(true)
            }

            val processBuilder = ProcessBuilder(
                qemuBin,
                "-m", ram,
                "-smp", "cores=$cpu",
                "-hda", diskImage,
                "-vnc", ":$vncPort",
                "-k", "en-us"
            )
            processBuilder.redirectErrorStream(true)
            vmProcess = processBuilder.start()

            // Optional: read output (for logging)
            vmProcess?.inputStream?.bufferedReader()?.use {
                it.lineSequence().forEach { line ->
                    // Can broadcast to UI via LiveData or Flow
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
            .setContentText("VM is running on VNC port $vncPort")
            .setSmallIcon(R.drawable.ic_launcher)
            .build()
    }

    fun getVncPort(): Int = vncPort
}
