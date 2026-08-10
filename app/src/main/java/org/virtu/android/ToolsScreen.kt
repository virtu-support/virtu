package org.virtu.android

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Plugin(
    val id: String,
    val name: String,
    val description: String,
    val installCommand: String
)

@Composable
fun ToolsScreen(
    onPluginInstall: (pluginId: String, command: String) -> Unit,
    modifier: Modifier = Modifier
) {
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
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(plugins) { plugin ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = plugin.description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        Button(
                            onClick = { onPluginInstall(plugin.id, plugin.installCommand) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Install")
                        }
                    }
                }
            }
        }
    }
}
