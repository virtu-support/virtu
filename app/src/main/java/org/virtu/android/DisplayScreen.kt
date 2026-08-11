package org.virtu.android

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.antlerstudios.android.vnc.VncView

@Composable
fun DisplayScreen(
    host: String = "127.0.0.1",
    port: Int = 5900,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vncView = remember { VncView(context) }

    DisposableEffect(Unit) {
        vncView.connect(host, port, "")
        vncView.setKeepAliveEnabled(true)

        onDispose {
            vncView.disconnect()
        }
    }

    AndroidView(
        factory = { vncView },
        modifier = modifier.fillMaxSize()
    )
}
