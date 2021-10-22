package com.lianshang.mvvm.util

import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lianshang.mvvm.R
import com.lianshang.mvvm.app.MyApp

/**
 *
 * Created by hong on 2021/9/30 16:39.
 *
 */
object HtmlUtil {
    fun setClickText(vText: TextView, htmlText: String?) {
        setClickText(vText, htmlText, null)
    }


    fun setClickText(vText: TextView, htmlText: String?, onUrlClick: OnUrlClickListener?) {
        vText.text = Html.fromHtml(htmlText)
        vText.movementMethod = LinkMovementMethod.getInstance()
        parseLinkText(vText, onUrlClick)
    }

    private fun parseLinkText(vInfo: TextView, onUrlClick: OnUrlClickListener?) {
        vInfo.highlightColor = 0
        val text = vInfo.text
        if (text is Spannable) {
            val end = text.length
            val sp = vInfo.text as Spannable
            val urls = sp.getSpans(
                0, end,
                URLSpan::class.java
            )
            if (urls.isEmpty()) return
            val style = SpannableStringBuilder(text)
            //style.clearSpans();// 这里会清除之前所有的样式
            for (url in urls) {
                style.removeSpan(url) //只需要移除之前的URL样式，再重新设置
                val myURLSpan = NoLineClickSpan(url.url, onUrlClick)
                style.setSpan(
                    myURLSpan,
                    sp.getSpanStart(url),
                    sp.getSpanEnd(url),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            vInfo.text = style
        }
    }


    fun getClickText(url: String, name: String): String? {
        return "<a href=\"$url\">$name</a>"
    }


    interface OnUrlClickListener {
        fun onClick(url: String?)
    }

    private class NoLineClickSpan internal constructor(
        private val mUrl: String,
        private val onUrlClick: OnUrlClickListener?
    ) :
        ClickableSpan() {
        override fun onClick(widget: View) {
            try {
                onUrlClick?.onClick(mUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(MyApp.getContext(), R.color.font_yellow_color)
            ds.isUnderlineText = false //去掉下划线
            ds.clearShadowLayer()
        }
    }
}