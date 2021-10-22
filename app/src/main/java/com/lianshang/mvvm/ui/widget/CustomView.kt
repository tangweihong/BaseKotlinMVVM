package com.lianshang.mvvm.ui.widget

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi

/**
 *
 * Created by hong on 2021/7/23 14:52.
 *
 */
class CustomView : View {
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    // 高阶函数
    var callBack: ((Int) -> Boolean)? = null
    var pregress = 0
        set(value) {
            field = value
            Log.v("yk", "我是set + $field")

        }

    private val mPaint1 by lazy {
        Paint().also {
            it.color = Color.GRAY
            it.style = Paint.Style.FILL
        }
    }
    private val mPaint2 by lazy {
        Paint().also {
            it.color = Color.BLUE
            it.style = Paint.Style.FILL
        }
    }
    private val mPaint3 by lazy {
        Paint().also {
            it.color = Color.BLUE
            it.style = Paint.Style.FILL
        }
    }

    //    画√
    private val mPaint4 by lazy {
        Paint().also {
            it.color = Color.WHITE
            it.style = Paint.Style.STROKE
            it.strokeWidth = 5f
            it.strokeCap = Paint.Cap.ROUND;
        }
    }

    //    线
    private var line1X: Float = 0f
    private var line1Y: Float = 0f
    private var line2X: Float = 0f
    private var line2Y: Float = 0f

    //    保存图片的宽高
    private var ykWidth: Float = 0f
    private var ykHeight: Float = 0f

    //    第一次图层的y坐标
    private var firstX: Float = 0f

    //    第二次图片的x 坐标
    private var seondX: Float = 0f
    private var seconY: Float = 0f

    //    第二个图层两端缩进
    private var secondStartX: Float = 0f
    private var secondEndX: Float = 0f

    //    保存图集
    private var animatorSet = AnimatorSet()

    //    圆角矩形的rect
    private var oval3 = RectF(0f, 0f, 0f, 0f)

    //    圆角矩形的圆角
    private var round: Float = 0f

    //    圆形的半径
    private var circleRadious: Float = 0f

    //    测量控件本身的尺寸
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ykWidth = width.toFloat()
        ykHeight = height.toFloat()
        firstX = height.toFloat()
        oval3 = RectF(0f, 0f, ykWidth, ykHeight) // 设置个新的长方形
        seconY = height.toFloat()

//        线
        line1X = ykWidth / 2 - 60f
        line1Y = ykHeight / 2 - 30f
        line2X = ykWidth / 2 - 20f
        line2Y = ykHeight / 2 + 30f
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, ykWidth, firstX, mPaint1)
        canvas?.drawRoundRect(secondStartX, 0f, secondEndX, seconY, round, round, mPaint2)

//        栏条消失，画一个蓝色圆
        canvas?.drawCircle(ykWidth / 2.toFloat(), ykHeight / 2, circleRadious, mPaint3)

//        画√
//        canvas?.drawPath(mPath4,mPaint4)

        canvas?.drawLine(ykWidth / 2 - 60f, ykHeight / 2 - 30f, line1X, line1Y, mPaint4)
        canvas?.drawLine(ykWidth / 2 - 20f, ykHeight / 2 + 30f, line2X, line2Y, mPaint4)

    }

    //    Start启动动画的函数，在主函数中调用(
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun start() {
//        填充蓝条
        val animator1 = ValueAnimator.ofFloat(0f, ykWidth).also {
            it.duration = 700
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                secondEndX = yk.animatedValue as Float
                invalidate()
            }
        }

//        改变灰色图层的y坐标(让其消失）
        val animator2 = ValueAnimator.ofFloat(ykHeight, 0f).also {
            it.duration = 0
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                firstX = yk.animatedValue as Float
                invalidate()
            }
        }

//        蓝条两边缩进
        val animator4 = ValueAnimator.ofFloat(0f, 170f).also {
            it.duration = 500
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                round = yk.animatedValue as Float
                invalidate()
            }
        }
        val animator5 = ValueAnimator.ofFloat(0f, ykWidth / 2).also {
            it.duration = 500
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                secondStartX = yk.animatedValue as Float
                invalidate()
            }
        }
        val animator6 = ValueAnimator.ofFloat(ykWidth, ykWidth / 2).also {
            it.duration = 500
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                secondEndX = yk.animatedValue as Float
                invalidate()
            }
        }

//        画圆
        val animator7 = ValueAnimator.ofFloat(0f, ykHeight / 2).also {
            it.duration = 100
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                circleRadious = yk.animatedValue as Float
                invalidate()
            }
        }

//        画√(线1）
        val animator8 = ValueAnimator.ofFloat(ykWidth / 2 - 60f, ykWidth / 2 - 20f).also {
            it.duration = 500
            it.repeatCount = 0
            it.startDelay = 500
            it.addUpdateListener { yk ->
                line1X = yk.animatedValue as Float
                invalidate()
            }
        }
        val animator9 = ValueAnimator.ofFloat(ykHeight / 2 - 30f, ykHeight / 2 + 30f).also {
            it.duration = 500
            it.repeatCount = 0
            it.startDelay = 500
            it.addUpdateListener { yk ->
                line1Y = yk.animatedValue as Float
                invalidate()
            }
        }
//        线2
        val animator10 = ValueAnimator.ofFloat(ykWidth / 2 - 20f, ykWidth / 2 + 80f).also {
            it.duration = 500
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                line2X = yk.animatedValue as Float
                invalidate()
            }
        }
        val animator11 = ValueAnimator.ofFloat(ykHeight / 2 + 30f, ykHeight / 2 - 50f).also {
            it.duration = 500
            it.repeatCount = 0
            it.addUpdateListener { yk ->
                line2Y = yk.animatedValue as Float
                invalidate()
            }
        }

        animatorSet.playSequentially(animator1, animator2, animator4, animator5)
        animatorSet.playTogether(animator5, animator6, animator7)
        animatorSet.playSequentially(animator7, animator8)
        animatorSet.playTogether(animator8, animator9)
        animatorSet.playSequentially(animator9, animator10)
        animatorSet.playTogether(animator10, animator11)
        if (animatorSet.isPaused) {
            animatorSet.resume()
        } else {
            animatorSet.start()

        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun stop() {
        animatorSet.end()
    }
}