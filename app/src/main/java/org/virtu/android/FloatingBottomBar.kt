package org.virtu.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FloatingBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    // 5 tabs: Home, Distros, Terminal, Tools, Settings
    val items = listOf(
        Triple("Home", Icons.Default.Home, 0),
        Triple("Distros", Icons.Default.SdCard, 1),
        Triple("Terminal", Icons.Default.Terminal, 2),
        Triple("Tools", Icons.Default.Build, 3),
        Triple("Settings", Icons.Default.Settings, 4)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp)
                .widthIn(max = 480.dp)
                .height(72.dp)
                .padding(horizontal = 4.dp),
            color = colorScheme.surface.copy(alpha = 0.92f),
            shape = RoundedCornerShape(36.dp),
            shadowElevation = 0.dp,
            border = androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = colorScheme.outline.copy(alpha = 0.75f)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { (label, icon, index) ->
                    val selected = selectedTab == index
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onTabSelected(index) }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (selected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            color = if (selected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.6f),
                            maxLines = 1,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
