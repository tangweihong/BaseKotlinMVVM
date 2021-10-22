package com.lianshang.mvvm.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class NoPaddingTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): AppCompatTextView(context,attrs,defStyleAttr) {
    private val rect = Rect()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateTextParams()
        setMeasuredDimension(rect.right - rect.left, -rect.top + rect.bottom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        drawText(canvas)
    }

    private fun calculateTextParams(): String {

        var text = text.toString()

        if (TextUtils.isEmpty(text)) {
            text = ""
        }
        val textLength = text.length

        paint.getTextBounds(text, 0, textLength, rect)

        if (textLength == 0) {

            rect.right = rect.left

        }

        return text

    }

    private fun drawText(canvas: Canvas?) {

        val text = calculateTextParams()

        val left = rect.left

        val bottom = rect.bottom

        rect.offset(-rect.left, -rect.top)

        paint.isAntiAlias = true

        paint.color = currentTextColor

        canvas?.drawText(text, (-left).toFloat(), (rect.bottom - bottom).toFloat(), paint)
    }
}
