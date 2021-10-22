package com.lianshang.mvvm.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.htt.base_library.base.BaseFragment
import com.htt.base_library.base.BaseLazyFragment
import com.htt.base_library.base.BaseVMFragment
import com.htt.base_library.base.NoViewModel
import com.htt.base_library.util.showToast
import com.lianshang.mvvm.databinding.FragmentWalletBinding
import com.lianshang.mvvm.util.HtmlUtil
import com.lianshang.mvvm.util.ImageSaveUtil

class MainProjectFragment : BaseLazyFragment<NoViewModel, FragmentWalletBinding>() {

    override fun lazyLoad() {
//        showLoadingDialog("加载中...")
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {

        immersionBar {
            titleBar(mViewBinding.includeToolbar.toolbar)
            statusBarDarkFont(true, 0.2f)}
        var tab1 = TestFragment()
        var tab2 = TestFragment()
        var tab3 = TestFragment()
        var lsit = listOf<Fragment>(tab1, tab2, tab3)
        mViewBinding.viewpager.adapter = KotlinPagerAdapter(lsit, childFragmentManager)
        mViewBinding.tabLayout.setupWithViewPager(mViewBinding.viewpager)
        //
        val list = listOf<String>("推荐", "订阅", "收藏")
        mViewBinding.tabLayout.getTabAt(0)?.text = list[0]
        mViewBinding.tabLayout.getTabAt(1)?.text = list[1]
        mViewBinding.tabLayout.getTabAt(2)?.text = list[2]
    }

    @SuppressLint("WrongConstant")
    class KotlinPagerAdapter(var mList: List<Fragment>, fm: FragmentManager) :
        FragmentPagerAdapter(fm, 0) {

        override fun getItem(position: Int): Fragment {
            return mList[position]
        }

        override fun getCount(): Int {
            return mList.size
        }

    }

    override fun setEventListener() {

    }

    override fun loadData() {

    }
    override fun onResume() {
        super.onResume()
        LogUtils.e("onResume")
    }
}