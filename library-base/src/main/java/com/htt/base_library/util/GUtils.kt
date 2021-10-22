package com.htt.base_library.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.regex.Pattern


/**个性化拓展函数*/
fun showToast(msg: String?) {
    ToastUtils.make().setMode(ToastUtils.MODE.DARK).show(msg)
//    ToastUtils.showShort(msg)
}

fun showToast(@StringRes msgId: Int) {
    ToastUtils.showShort(msgId)
}


/**
 * base64 数据流图片  用于图片验证码
 */
fun base64Image(base64Data: String): Bitmap {
    val bytes = Base64.decode(base64Data, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Any.toJson(): String = Gson().toJson(this)

/**
 * 判断是字母
 */
fun String?.isLetterDigit(): Boolean = this?.let {
    return Pattern.compile("[a-zA-Z]+").matcher(it).matches()
} ?: false

/**
 * 判断是否是数字
 */
fun String?.isNumeric(): Boolean = this?.let {
    return Pattern.compile("[0-9]*").matcher(it).matches()
} ?: false



/**
 * 复制文本
 */
fun String?.copyString(): Boolean {
    if (isNullOrBlank()) {
        return false
    }
    // 获取系统剪贴板
    val clipboard = Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
    val clipData = ClipData.newPlainText(null, this)
    // 把数据集设置（复制）到剪贴板
    clipboard.setPrimaryClip(clipData)
    return true
}

val Number.double_2: String
    get() {
        val format = DecimalFormat("0.00")//构造方法的字符格式这里如果小数不足4位,会以0补足.
        format.roundingMode = RoundingMode.DOWN
        return format.format(toDouble())//format 返回的是字符串
    }
val Number.double_4: String
    get() {
        val format = DecimalFormat("0.0000")//构造方法的字符格式这里如果小数不足4位,会以0补足.
        format.roundingMode = RoundingMode.DOWN
        return format.format(toDouble())//format 返回的是字符串
    }

val String?.double_2: String
    get() {
        return (this?.toDoubleOrNull() ?: 0.00).double_2
    }
val String?.double_4: String
    get() {
        return (this?.toDoubleOrNull() ?: 0.00).double_4
    }
val String?.double_0: String
    get() {
        return (this?.toDoubleOrNull() ?: 0.00).toInt().toString()
    }

