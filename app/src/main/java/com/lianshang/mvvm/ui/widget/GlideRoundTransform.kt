package com.lianshang.mvvm.ui.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest


class GlideRoundTransform(context: Context?, dp: Int) :
    BitmapTransformation() {
    constructor(context: Context?) : this(context, 4) {}

    val id: String
        get() = javaClass.name + Math.round(radius)

    companion object {
        private var radius = 0f
        private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
            if (source == null) return null
            var result: Bitmap =
                pool[source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888]
            if (result == null) {
                result = Bitmap.createBitmap(
                    source.getWidth(),
                    source.getHeight(),
                    Bitmap.Config.ARGB_8888
                )
            }
            val canvas = Canvas(result)
            val paint = Paint()
            paint.shader = BitmapShader(
                source,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
            )
            paint.setAntiAlias(true)
            val rectF = RectF(0f, 0f, source.getWidth().toFloat(), source.getHeight().toFloat())
            canvas.drawRoundRect(rectF, radius, radius, paint)
            return result
        }
    }

    init {
//        super(context);
        radius = Resources.getSystem().getDisplayMetrics().density * dp
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        val bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        return roundCrop(pool, bitmap)
    }
}