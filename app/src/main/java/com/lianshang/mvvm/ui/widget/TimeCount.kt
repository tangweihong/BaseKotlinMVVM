package com.lianshang.mvvm.ui.widget

import android.os.CountDownTimer
import android.widget.TextView
import com.lianshang.mvvm.R

class TimeCount :CountDownTimer{

    var textView:TextView

    constructor(millisInFuture:Long,
                countDownInterval:Long,
                view: TextView
    ):super(millisInFuture,countDownInterval){
        this.textView=view;
    }

    override fun onTick(millisUntilFinished: Long) {
        if(textView.isEnabled) textView.isEnabled=false
        textView.setTag(R.id.tag_id,1)
        textView.text="${millisUntilFinished/1000}秒"

    }

    override fun onFinish() {
        textView.setTag(R.id.tag_id,0)
        textView.isEnabled=true
        textView.text="获取验证码"
    }
}