package org.virtu.android.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun copyAssetToFile(context: Context, assetPath: String, destination: File): Boolean {
        return try {
            context.assets.open(assetPath).use { input ->
                FileOutputStream(destination).use { output ->
                    input.copyTo(output)
                }
            }
            destination.setExecutable(true)
            true
        } catch (e: Exception) {
            false
        }
    }
}
