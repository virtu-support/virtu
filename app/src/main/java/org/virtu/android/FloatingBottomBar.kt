package org.virtu.android

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.cardview.widget.CardView

class FloatingBottomBar(context: Context) : CardView(context) {

    private val buttonLayout: LinearLayout
    val startButton: Button
    val stopButton: Button
    val testButton: Button

    init {
        // CardView setup
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
            setMargins(32, 0, 32, 32)
        }
        radius = 32f
        elevation = 16f
        setCardBackgroundColor(android.graphics.Color.WHITE)

        // Linear layout for buttons
        buttonLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(24, 16, 24, 16)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Create buttons
        startButton = Button(context).apply {
            text = "Start"
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply { setMargins(0, 0, 16, 0) }
        }

        stopButton = Button(context).apply {
            text = "Stop"
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply { setMargins(0, 0, 16, 0) }
        }

        testButton = Button(context).apply {
            text = "Test"
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        // Add buttons to layout
        buttonLayout.addView(startButton)
        buttonLayout.addView(stopButton)
        buttonLayout.addView(testButton)

        // Add layout to card
        addView(buttonLayout)
    }
}
