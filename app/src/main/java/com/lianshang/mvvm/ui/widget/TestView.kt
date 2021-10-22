package com.lianshang.mvvm.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

/**
 *
 * Created by hong on 2021/7/28 10:45.
 *
 */
class TestView : View {
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val mPaint1 by lazy {
        Paint().also {
            it.color = Color.GRAY
            it.style = Paint.Style.FILL
        }
    }
    private val mPaint2 by lazy {
        Paint().also {
            it.color = Color.BLUE
            it.strokeWidth = 2f
            it.style = Paint.Style.FILL
        }
    }

    //    保存图片的宽高
    private var ykWidth: Float = 0f
    private var ykHeight: Float = 0f

    //    第一次图层的y坐标
    private var firstX: Float = 0f

    //    测量控件本身的尺寸
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ykWidth = width.toFloat()
        ykHeight = height.toFloat()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(0f, ykHeight - 1f, ykWidth, ykHeight - 1f, mPaint2);
        canvas?.drawLine(0f, 0f, 0f, ykHeight, mPaint2)

    }

}