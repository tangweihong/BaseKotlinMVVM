package com.htt.base_library.view.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.test.library_base.R

class ResizeImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    var resizeW=16
    var resizeH=9

    init {
        attrs?.let {
            var ta=context.obtainStyledAttributes(it, R.styleable.ResizeImageView)

            resizeW=ta.getInt(R.styleable.ResizeImageView_resize_width,16)
            resizeH=ta.getInt(R.styleable.ResizeImageView_resize_height,9)

            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height: Int = width * resizeH / resizeW
        setMeasuredDimension(width, height)
    }
}