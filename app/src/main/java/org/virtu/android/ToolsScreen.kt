package org.virtu.android

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Data class for a plugin
data class Plugin(
    val id: String,
    val name: String,
    val description: String,
    val installCommand: String // curl command or script URL
)

@Composable
fun ToolsScreen(
    onPluginInstall: (pluginId: String, command: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Sample plugins (replace with real data from your repo later)
    val plugins = listOf(
        Plugin(
            id = "mouse",
            name = "Mouse Support",
            description = "Adds mouse emulation for VNC",
            installCommand = "curl -sSL https://raw.githubusercontent.com/virtu-support/plugins/main/mouse.sh | sh"
        ),
        Plugin(
            id = "audio",
            name = "Audio Driver",
            description = "Enable audio forwarding for QEMU",
            installCommand = "curl -sSL https://raw.githubusercontent.com/virtu-support/plugins/main/audio.sh | sh"
        ),
        Plugin(
            id = "clipboard",
            name = "Shared Clipboard",
            description = "Sync clipboard between Android and VM",
            installCommand = "curl -sSL https://raw.githubusercontent.com/virtu-support/plugins/main/clipboard.sh | sh"
        ),
        Plugin(
            id = "extra-keys",
            name = "Extra Keys",
            description = "Add Ctrl, Alt, Win keys to the VNC keyboard",
            installCommand = "curl -sSL https://raw.githubusercontent.com/virtu-support/plugins/main/extra-keys.sh | sh"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(plugins) { plugin ->
            PluginCard(
                plugin = plugin,
                onInstall = { onPluginInstall(plugin.id, plugin.installCommand) }
            )
        }
    }
}

@Composable
fun PluginCard(
    plugin: Plugin,
    onInstall: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = plugin.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = plugin.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Button(
                onClick = onInstall,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Install")
            }
        }
    }
}
