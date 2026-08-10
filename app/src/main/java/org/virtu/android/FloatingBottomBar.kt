package org.virtu.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FloatingBottomBar(
    selectedAction: String,
    onActionClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val actions = listOf(
        Triple("Start", R.drawable.ic_media_play, "Start"),
        Triple("Stop", android.R.drawable.ic_menu_close_clear_cancel, "Stop"),
        Triple("Test", android.R.drawable.ic_menu_manage, "Test")
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()          // NOT fillMaxWidth – only as wide as needed
                .padding(horizontal = 16.dp) // Margin from screen edges
                .widthIn(max = 420.dp)       // Max width, but can be smaller
                .height(64.dp)
                .padding(horizontal = 8.dp), // Internal spacing
            color = colorScheme.surface.copy(alpha = 0.92f),
            shape = RoundedCornerShape(32.dp),
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = colorScheme.outline.copy(alpha = 0.15f)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions.forEach { (label, iconRes, action) ->
                    val selected = selectedAction == action
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onActionClick(action) }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(iconRes),
                            contentDescription = label,
                            tint = if (selected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = label,
                            fontSize = 11.sp,
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
