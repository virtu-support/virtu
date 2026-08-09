package org.virtu.android

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class FloatingBottomBar(context: Context) : FrameLayout(context) {

    val statusText: TextView
    private val startTile: LinearLayout
    private val stopTile: LinearLayout
    private val testTile: LinearLayout

    var onStartClick: (() -> Unit)? = null
    var onStopClick: (() -> Unit)? = null
    var onTestClick: (() -> Unit)? = null

    init {
        // Root FrameLayout – fills width, centers its content
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.BOTTOM

        // ---- Card (Surface) ----
        val card = MaterialCardView(context).apply {
            // External margin: 16dp left + right (applied via layout params)
            val marginPx = (16 * resources.displayMetrics.density).toInt()
            val maxWidthPx = (420 * resources.displayMetrics.density).toInt()
            val heightPx = (68 * resources.displayMetrics.density).toInt()

            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                heightPx
            ).apply {
                gravity = Gravity.CENTER
                // Set margins to avoid touching edges
                setMargins(marginPx, 0, marginPx, 0)
                // Max width: we use a FrameLayout with center gravity, but we need to limit width.
                // Since we set MATCH_PARENT and margins, on large screens the card still stretches.
                // To enforce max width, we wrap the card in a container with max width.
                // However, we can simply set the card's width to a fixed max width and center it.
                // We'll set width to maxWidthPx and center horizontally.
                width = maxWidthPx
                // Also center the card: we set gravity to CENTER in the parent.
            }

            // Surface attributes
            radius = 34f
            elevation = 10f
            // Background with alpha 0.85 (217/255)
            val bgColor = Color.argb(217, 255, 255, 255)
            setCardBackgroundColor(bgColor)

            // Border stroke (1dp, subtle)
            val strokeWidth = (1 * resources.displayMetrics.density).toInt()
            val borderColor = Color.argb(30, 0, 0, 0)
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = radius
                setStroke(strokeWidth, borderColor)
                setColor(bgColor)
            }
            background = drawable
        }

        // Inner container – applies the internal horizontal padding (4dp)
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            val innerPad = (4 * resources.displayMetrics.density).toInt()
            setPadding(innerPad, 0, innerPad, 0)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Row for three tiles (like the Row in Compose)
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Helper to create each tile (icon + label)
        fun createTile(iconRes: Int, label: String): LinearLayout {
            return LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
                val icon = ImageView(context).apply {
                    setImageResource(iconRes)
                    setColorFilter(Color.DKGRAY)
                    layoutParams = LinearLayout.LayoutParams(32, 32)
                }
                addView(icon)
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
                isClickable = true
                isFocusable = true
                // Simple ripple-like effect from default button background
                background = context.getDrawable(android.R.drawable.btn_default)
            }
        }

        startTile = createTile(android.R.drawable.ic_media_play, "Start")
        stopTile = createTile(android.R.drawable.ic_menu_close_clear_cancel, "Stop")
        testTile = createTile(android.R.drawable.ic_menu_manage, "Test")

        startTile.setOnClickListener { onStartClick?.invoke() }
        stopTile.setOnClickListener { onStopClick?.invoke() }
        testTile.setOnClickListener { onTestClick?.invoke() }

        row.addView(startTile)
        row.addView(stopTile)
        row.addView(testTile)

        container.addView(row)
        card.addView(container)

        // Add card to root
        addView(card)

        // ---- Status text below the card ----
        statusText = TextView(context).apply {
            text = "Ready"
            textSize = 14f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = (8 * resources.displayMetrics.density).toInt()
            }
        }
        // Add status text with bottom margin
        addView(statusText)
        // Give card some bottom margin so it doesn't overlap status text
        (card.layoutParams as FrameLayout.LayoutParams).bottomMargin = (40 * resources.displayMetrics.density).toInt()
    }

    // Highlight selected tile
    fun setSelected(selected: String) {
        val items = listOf(startTile to "Start", stopTile to "Stop", testTile to "Test")
        items.forEach { (tile, label) ->
            val icon = tile.getChildAt(0) as ImageView
            val text = tile.getChildAt(1) as TextView
            val isSelected = label == selected
            val color = if (isSelected) Color.parseColor("#6200EE") else Color.DKGRAY
            icon.setColorFilter(color)
            text.setTextColor(color)
        }
    }
}
