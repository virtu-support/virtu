package org.virtu.android

class VmEngine {
    companion object {
        init {
            System.loadLibrary("virtu")
        }
    }

    external fun runCommand(command: String): Int
}
