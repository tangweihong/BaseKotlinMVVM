package com.lianshang.mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.htt.base_library.base.BaseLazyFragment
import com.htt.base_library.base.BaseVMFragment
import com.htt.base_library.util.showToast
import com.lianshang.mvvm.databinding.FragmentMiningBinding
import com.lianshang.mvvm.ext.animateWidth
import com.lianshang.mvvm.ext.animateWidthAndHeight
import com.lianshang.mvvm.ui.viewmodel.MainViewModel
import java.math.BigDecimal
import kotlin.math.min
import kotlin.random.Random

class MainSquareFragment : BaseLazyFragment<MainViewModel, FragmentMiningBinding>() {

    override fun lazyLoad() {
        viewModel.setMainTabIndex("我的页面提交")
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        immersionBar {
            titleBar(mViewBinding.includeToolbar.toolbar)
            statusBarDarkFont(true, 0.2f)}
    }

    override fun setEventListener() {

    }


    override fun loadData() {
        viewModel.mMainIndex.observe(this,object :Observer<String>{
            override fun onChanged(t: String?) {
                showToast("弹窗提示 $t")
            }

        })
//        viewModel.mMainIndex.observe(this) {
//            showToast("弹窗提示 $it")
//        }
    }

    override fun onResume() {
        super.onResume()
        LogUtils.e("onResume")
    }
}