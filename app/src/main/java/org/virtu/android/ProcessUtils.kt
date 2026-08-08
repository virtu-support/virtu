package org.virtu.android.utils

import java.io.BufferedReader
import java.io.InputStreamReader

object ProcessUtils {
    fun runCommand(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            process.waitFor()
            output
        } catch (e: Exception) {
            e.message ?: "Error"
        }
    }
}
