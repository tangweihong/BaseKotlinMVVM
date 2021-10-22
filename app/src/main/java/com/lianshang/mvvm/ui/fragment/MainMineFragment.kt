package com.lianshang.mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.htt.base_library.base.BaseLazyFragment
import com.htt.base_library.base.BaseVMFragment
import com.htt.base_library.base.NoViewModel
import com.htt.base_library.util.showToast
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.FragmentMineBinding
import com.lianshang.mvvm.ui.viewmodel.MainViewModel
import java.math.BigDecimal

class MainMineFragment : BaseLazyFragment<MainViewModel, FragmentMineBinding>() {

    override fun lazyLoad() {
//        showLoadingDialog("加载中...")
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        immersionBar {
            statusBarView(mViewBinding.toolbarLine)
            statusBarDarkFont(true, 0.2f)
            init()
        }
    }

    override fun setEventListener() {
        mViewBinding.mineImgAssetsVsb.setOnClickListener(listener)
        ClickUtils.applySingleDebouncing(mViewBinding.mineTvBill, listener)
    }

    private var listener = View.OnClickListener {
        when (it.id) {
            R.id.mine_img_assets_vsb -> {
                if (mViewBinding.mineImgAssetsVsb.isSelected) {
                    mViewBinding.mineImgAssetsVsb.isSelected = false
                    mViewBinding.mineTvAssetsTotal.text = "****"
                } else {
                    mViewBinding.mineImgAssetsVsb.isSelected = true
                    mViewBinding.mineTvAssetsTotal.text =
                        BigDecimal("100").setScale(6, BigDecimal.ROUND_DOWN)
                            .toPlainString() + " USDT"
                }
            }
            R.id.mine_tv_bill -> {
                viewModel.setMainTabIndex("我的页面提交")
            }
        }
    }
    override fun onResume() {
        super.onResume()
        LogUtils.e("onResume")
    }
    override fun loadData() {

    }


}