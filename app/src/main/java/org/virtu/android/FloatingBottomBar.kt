package org.virtu.android

import android.graphics.Color
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
        Triple("Stop", R.drawable.ic_menu_close_clear_cancel, "Stop"),
        Triple("Test", R.drawable.ic_menu_manage, "Test")
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // external margin
                .widthIn(max = 420.dp)
                .height(68.dp)
                .padding(horizontal = 4.dp), // internal padding
            color = colorScheme.background.copy(alpha = 0.85f),
            shape = RoundedCornerShape(34.dp),
            shadowElevation = 10.dp,
            tonalElevation = 0.dp,
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = colorScheme.onBackground.copy(alpha = 0.12f)
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
                            tint = if (selected) colorScheme.primary else colorScheme.onBackground,
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            color = if (selected) colorScheme.primary else colorScheme.onBackground,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
