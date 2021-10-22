package com.lianshang.mvvm.util

import android.view.Gravity
import android.view.LayoutInflater
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.LayoutToastResultBinding
import com.lianshang.mvvm.databinding.LayoutToastTextBinding

object ToastUtil {

    fun showOkToast(msg: String, showLong: Boolean = false) {
        showToast(msg = msg, showLong = showLong)
    }

    fun showFailToast(msg: String) {
        showToast(R.mipmap.ic_toast_fail, msg)
    }

    private fun showToast(res: Int = R.mipmap.ic_toast_ok, msg: String, showLong: Boolean = false) {
//        var view= View.inflate(Utils.getApp(),R.layout.layout_toast_result,null)
        var binding = LayoutToastResultBinding.inflate(LayoutInflater.from(Utils.getApp()))
        binding.tvMsg.text = msg
        binding.ivIcon.setImageResource(res)
        ToastUtils.make()
            .setDurationIsLong(showLong)
            .setGravity(Gravity.CENTER, 0, 0)
            .show(binding.root)
    }

    fun showShortMsg(msg: String) {
//        var view = View.inflate(Utils.getApp(), R.layout.layout_toast_text, null)
        val binding = LayoutToastTextBinding.inflate(LayoutInflater.from(Utils.getApp()))
        binding.tvToastMsg.text = msg
        ToastUtils.make()
            .setGravity(Gravity.BOTTOM, 0, 300)
            .show(binding.root)
    }
}