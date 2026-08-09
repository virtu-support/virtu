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
import com.google.android.material.card.MaterialCardView

class FloatingBottomBar(context: Context) : FrameLayout(context) {

    val statusText: TextView
    private val startTile: LinearLayout
    private val stopTile: LinearLayout
    private val testTile: LinearLayout

    var onStartClick: (() -> Unit)? = null
    var onStopClick: (() -> Unit)? = null
    var onTestClick: (() -> Unit)? = null

    init {
        // Root FrameLayout – fills the parent, children will be placed with gravity in their layout params
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // ---- Card (Surface) ----
        val card = MaterialCardView(context).apply {
            val marginPx = (16 * resources.displayMetrics.density).toInt()
            val maxWidthPx = (420 * resources.displayMetrics.density).toInt()
            val heightPx = (68 * resources.displayMetrics.density).toInt()

            // Place the card at the bottom, centered horizontally
            layoutParams = FrameLayout.LayoutParams(
                maxWidthPx,
                heightPx
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                setMargins(marginPx, 0, marginPx, 0)
            }

            radius = 34f
            elevation = 10f
            val bgColor = Color.argb(217, 255, 255, 255)
            setCardBackgroundColor(bgColor)

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

        // Row for three tiles
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

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
                // Position it just below the card
                topMargin = (68 * resources.displayMetrics.density).toInt() + (16 * resources.displayMetrics.density).toInt()
            }
        }
        addView(statusText)
    }

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
