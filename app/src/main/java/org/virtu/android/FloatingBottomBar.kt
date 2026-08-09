package org.virtu.android

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

class FloatingBottomBar(context: Context) : MaterialCardView(context) {

    // Tiles and status
    private val startTile: LinearLayout
    private val stopTile: LinearLayout
    private val testTile: LinearLayout
    val statusText: TextView

    // Click listeners
    var onStartClick: (() -> Unit)? = null
    var onStopClick: (() -> Unit)? = null
    var onTestClick: (() -> Unit)? = null

    init {
        // Card setup – matches Compose Surface
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
            setMargins(32, 0, 32, 32)
        }
        radius = 34f                     // RoundedCornerShape(34.dp)
        elevation = 10f                  // shadowElevation = 10.dp
        setCardBackgroundColor(Color.WHITE) // background.copy(alpha = 0.85f) – we use white with slight transparency
        // For alpha: we'll set a translucent color (optional)
        // setCardBackgroundColor(Color.argb(217, 255, 255, 255)) // 85% white

        // Border (like BorderStroke)
        // MaterialCardView doesn't have a direct border, so we'll use a stroke drawable or a background.
        // We'll skip border to keep it simple.

        // Main container (vertical)
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(16, 12, 16, 12)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Row for three items (Start, Stop, Test) – like the Row in Compose
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Helper to create each item (like the Column in Compose)
        fun createItem(iconRes: Int, label: String): LinearLayout {
            return LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
                // Icon
                val icon = ImageView(context).apply {
                    setImageResource(iconRes)
                    setColorFilter(Color.DKGRAY) // default unselected color
                    layoutParams = LinearLayout.LayoutParams(32, 32)
                }
                addView(icon)
                // Text
                val text = TextView(context).apply {
                    this.text = label
                    textSize = 11f
                    setTextColor(Color.DKGRAY)
                    typeface = android.graphics.Typeface.DEFAULT
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { topMargin = 4 }
                }
                addView(text)
                // Clickable
                isClickable = true
                isFocusable = true
                // Selected state handling will be done via listeners
            }
        }

        startTile = createItem(android.R.drawable.ic_media_play, "Start")
        stopTile = createItem(android.R.drawable.ic_menu_close_clear_cancel, "Stop")
        testTile = createItem(android.R.drawable.ic_menu_manage, "Test")

        // Click listeners
        startTile.setOnClickListener { onStartClick?.invoke() }
        stopTile.setOnClickListener { onStopClick?.invoke() }
        testTile.setOnClickListener { onTestClick?.invoke() }

        // Add tiles to row
        row.addView(startTile)
        row.addView(stopTile)
        row.addView(testTile)

        container.addView(row)
        addView(container)

        // Status text (optional, we can add it below or above)
        // For now we'll keep it as a separate field that can be updated externally.
        statusText = TextView(context).apply {
            text = "Ready"
            textSize = 12f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                setMargins(0, 0, 0, 8)
            }
        }
        addView(statusText)
    }

    // Method to update selected state (like in Compose)
    fun setSelected(selected: String) {
        val items = listOf(startTile to "Start", stopTile to "Stop", testTile to "Test")
        items.forEach { (tile, label) ->
            val icon = tile.getChildAt(0) as ImageView
            val text = tile.getChildAt(1) as TextView
            val isSelected = label == selected
            icon.setColorFilter(if (isSelected) Color.parseColor("#6200EE") else Color.DKGRAY)
            text.setTextColor(if (isSelected) Color.parseColor("#6200EE") else Color.DKGRAY)
        }
    }
}
