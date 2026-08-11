package org.virtu.android

import java.io.File
import java.io.FileInputStream
import java.util.Properties

object VmConfig {

    private const val CONFIG_PATH = "/storage/emulated/0/virtu/vm.conf"

    fun load(): Properties {
        val props = Properties()
        val configFile = File(CONFIG_PATH)
        if (configFile.exists()) {
            try {
                FileInputStream(configFile).use { props.load(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // Defaults if not present
        if (!props.containsKey("RAM")) props.setProperty("RAM", "1024")
        if (!props.containsKey("CPU")) props.setProperty("CPU", "2")
        if (!props.containsKey("VNC_PORT")) props.setProperty("VNC_PORT", "5900")
        if (!props.containsKey("DISK_PATH")) {
            props.setProperty("DISK_PATH", "/storage/emulated/0/virtu/distros/ubuntu/ubuntu.img")
        }
        if (!props.containsKey("QEMU_BIN")) {
            props.setProperty("QEMU_BIN", "/data/data/com.termux/files/usr/bin/qemu-system-x86_64")
        }
        return props
    }

    fun save(props: Properties) {
        try {
            File(CONFIG_PATH).outputStream().use { props.store(it, null) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
