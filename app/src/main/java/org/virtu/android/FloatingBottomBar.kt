package org.virtu.android

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class FloatingBottomBar(context: Context) : MaterialCardView(context) {

    val startButton: MaterialButton
    val stopButton: MaterialButton
    val testButton: MaterialButton
    val statusText: TextView

    init {
        // Card setup
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
            setMargins(32, 0, 32, 32)
        }
        radius = 28f
        elevation = 12f
        setCardBackgroundColor(Color.WHITE)
        isClickable = false

        // Main container (vertical)
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 20, 24, 20)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Title
        val title = TextView(context).apply {
            text = "virtu"
            textSize = 18f
            setTextColor(Color.BLACK)
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        container.addView(title)

        // Status text (updates from MainActivity)
        statusText = TextView(context).apply {
            text = "Ready"
            textSize = 14f
            setTextColor(Color.GRAY)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 4 }
        }
        container.addView(statusText)

        // Button row
        val buttonRow = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 16 }
        }

        // Start button
        startButton = MaterialButton(context).apply {
            text = "Start"
            icon = context.getDrawable(com.google.android.material.R.drawable.material_ic_play_arrow_black_24dp) // or use vector
            iconGravity = MaterialButton.ICON_GRAVITY_START
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply { setMargins(0, 0, 16, 0) }
            backgroundTintList = android.content.res.ColorStateList.valueOf(
                context.getColor(com.google.android.material.R.color.design_default_color_primary)
            )
            setTextColor(Color.WHITE)
        }
        buttonRow.addView(startButton)

        // Stop button
        stopButton = MaterialButton(context).apply {
            text = "Stop"
            icon = context.getDrawable(android.R.drawable.ic_menu_close_clear_cancel)
            iconGravity = MaterialButton.ICON_GRAVITY_START
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply { setMargins(0, 0, 16, 0) }
            backgroundTintList = android.content.res.ColorStateList.valueOf(
                context.getColor(android.R.color.holo_red_dark)
            )
            setTextColor(Color.WHITE)
        }
        buttonRow.addView(stopButton)

        // Test button
        testButton = MaterialButton(context).apply {
            text = "Test"
            icon = context.getDrawable(android.R.drawable.ic_menu_manage)
            iconGravity = MaterialButton.ICON_GRAVITY_START
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            backgroundTintList = android.content.res.ColorStateList.valueOf(
                context.getColor(android.R.color.holo_orange_dark)
            )
            setTextColor(Color.WHITE)
        }
        buttonRow.addView(testButton)

        container.addView(buttonRow)

        // Add container to card
        addView(container)
    }
}
