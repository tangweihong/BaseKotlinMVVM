package com.lianshang.mvvm.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.lianshang.mvvm.R
import com.lianshang.mvvm.util.CommonUtils

class FormatNumberView @kotlin.jvm.JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
):LinearLayout(context,attrs,defStyleAttr) {

    init {
        gravity=Gravity.BOTTOM
    }

    fun setFormatNumber(number:Long){
        var formatStr= CommonUtils.formatNumber(number)
        //遍历字符串
        if(childCount>0) removeAllViews()
        if(formatStr.isNotEmpty()){
            formatStr.forEachIndexed{index,chr ->
                if(chr==','){
                    var textView=TextView(context)
                    textView.text=chr.toString()
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25f)
                    textView.setTextColor(ColorUtils.getColor(R.color.white))
                    textView.includeFontPadding = false
                    var layoutParam=LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                    layoutParam.leftMargin= SizeUtils.dp2px(1f)
                    layoutParam.rightMargin=if(index==formatStr.length-1) 0 else SizeUtils.dp2px(3f)
                    layoutParam.bottomMargin=-SizeUtils.dp2px(3f)
                    addView(textView,layoutParam)
                }else{
                    var textView= View.inflate(context,R.layout.layout_number_text_item,null) as TextView
                    textView.text=chr.toString()
                    var layoutParam=LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                    layoutParam.leftMargin=0
                    layoutParam.rightMargin=if(index==formatStr.length-1) 0 else SizeUtils.dp2px(2f)
                    addView(textView,layoutParam)
                }
            }
            requestLayout()
        }
    }
}