package com.htt.base_library.util

import android.os.Looper
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Checkable
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * view点击事件
 */
//fun View.click(done: View.OnClickListener): Disposable {
//    return ViewClickObservable(this)
//        .throttleFirst(1, TimeUnit.SECONDS)
//        .subscribe {
//            done.onClick(it as? View)
//        }
//}
inline fun <T : View> T.click(time: Long = 800, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}
//兼容点击事件设置为this的情况
fun <T : View> T.click(onClickListener: View.OnClickListener, time: Long = 800) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

/**
 * editText输入监听
 */
inline fun TextView.textChanged(crossinline done: (s: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            done.invoke(sequence.toString())
        }
    })
}

/**
 * 部分文字高亮并添加点击事件
 */
inline fun TextView.spanClick(s: String, span: String, @ColorInt colorInt: Int, crossinline done: () -> Unit) {
    val spannableString = SpannableString(s)
    val pattern = Pattern.compile(span)
    val matcher: Matcher = pattern.matcher(s)
    while (matcher.find()) {
        val start = matcher.start()
        val end = matcher.end()
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(p0: View) {
                done.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = colorInt
                ds.isUnderlineText = false
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannableString
    }
    movementMethod = LinkMovementMethod.getInstance()
}

/**
 * 带判空获取正文
 *
 * @param default 默认 与hint只需要设置一个 如果正文为空
 * @param hint 提示文本 与default只需要设置一个
 * @return 正文
 */
fun TextView.getContentText(default: String? = null, hint: String? = null): String {
    return if (text.isNullOrBlank())
        default ?: hint?.let { throw NullPointerException(it) } ?: ""
    else
        text.toString()
}


