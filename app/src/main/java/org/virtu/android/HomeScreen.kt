package org.virtu.android

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Vm(
    val id: Int,
    val name: String,
    val lastRun: String,
    val status: String
)

@Composable
fun HomeScreen(
    onVmAction: (action: String, vmId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val vms = listOf(
        Vm(id = 1, name = "Ubuntu", lastRun = "2026-08-10 14:30", status = "stopped"),
        Vm(id = 2, name = "Windows 10", lastRun = "2026-08-09 22:15", status = "running"),
        Vm(id = 3, name = "Alpine Linux", lastRun = "2026-08-08 09:00", status = "paused")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(vms) { vm ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                VmCard(
                    vm = vm,
                    onAction = onVmAction,
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun VmCard(
    vm: Vm,
    onAction: (action: String, vmId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Computer,
                        contentDescription = "VM",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vm.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Last run: ${vm.lastRun}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "Status: ${vm.status}",
                    fontSize = 12.sp,
                    color = when (vm.status) {
                        "running" -> Color.Green
                        "paused" -> Color.Yellow
                        else -> Color.Red
                    }
                )
            }

            Row {
                listOf(
                    "play" to Icons.Default.PlayArrow,
                    "resume" to Icons.Default.Refresh,
                    "stop" to Icons.Default.Stop,
                    "freeze" to Icons.Default.Pause,
                    "settings" to Icons.Default.Settings
                ).forEach { (action, icon) ->
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        onClick = { onAction(action, vm.id) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = action,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}
