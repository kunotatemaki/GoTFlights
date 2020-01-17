package com.rookia.gotflights.ui.customviews


import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.rookia.gotflights.R
import kotlin.math.max


class CountDrawable(context: Context) :
    Drawable() {
    private val mBadgePaint: Paint
    private val mTextPaint: Paint
    private val mTxtRect = Rect()
    private var mCount = ""
    private var mWillDraw = false
    override fun draw(canvas: Canvas) {
        if (!mWillDraw) {
            return
        }
        val bounds = bounds
        val width = bounds.right - bounds.left.toFloat()
        val height = bounds.bottom - bounds.top.toFloat()
        // Position the badge in the top-right quadrant of the icon.
        val radius = max(width, height) / 2 / 2
        val centerX = width - radius - 1 + 5
        val centerY = radius - 5
        val extraSize = when(mCount.length){
            1 -> 8
            2 -> 10
            else -> 14
        }
        canvas.drawCircle(centerX, centerY, ((radius + extraSize)), mBadgePaint)
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length, mTxtRect)
        mTextPaint.color = Color.BLACK
        val textHeight = mTxtRect.bottom - mTxtRect.top.toFloat()
        val textY = centerY + textHeight / 2f
        if (mCount.length > 2) canvas.drawText(
            "99+",
            centerX,
            textY,
            mTextPaint
        ) else canvas.drawText(mCount, centerX, textY, mTextPaint)
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    fun setCount(count: String) {
        mCount = count
        // Only draw a badge if there are notifications.
        mWillDraw = !count.equals("0", ignoreCase = true)
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) { // do nothing
    }

    override fun setColorFilter(cf: ColorFilter?) { // do nothing
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    init {
        val mTextSize =
            context.resources.getDimension(R.dimen.badge_count_textsize)
        mBadgePaint = Paint()
        mBadgePaint.color = ContextCompat.getColor(
            context.applicationContext,
            R.color.badge_background_color
        )
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
        mTextPaint = Paint()
        mTextPaint.color = Color.WHITE
        mTextPaint.typeface = Typeface.DEFAULT
        mTextPaint.textSize = mTextSize
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
    }
}