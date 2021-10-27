package com.htt.base_library.base

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.htt.base_library.intrface.ISwipeRefreshView
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.test.library_base.R

/**
 *
 * Created by hong on 2021/7/13 9:46.
 *
 */
abstract class BaseSwipeRefreshFragment<VM : BaseVModel, VB : ViewBinding> :
    BaseLazyFragment<VM, VB>(), ISwipeRefreshView {

    open val vSwipeRefresh: SmartRefreshLayout? by lazy {
        mRootView.findViewById<SmartRefreshLayout>(getRefreshLayoutId())
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        vSwipeRefresh?.setRefreshHeader(setRefreshHeader())
        vSwipeRefresh?.setEnableLoadMore(false) //是否启用上拉加载功能
        vSwipeRefresh?.setOnRefreshListener(OnRefreshListener { startRefresh() })
    }

    open fun setRefreshHeader(): RefreshHeader {
        return MaterialHeader(mContext)
            .setColorSchemeColors(-0x52a7c, -0xbbbc, -0x996700, -0x559934, -0x7800)
    }

    /**
     * 设置仅越界回弹
     */
    protected open fun setOnlyScrollBounce() {
        vSwipeRefresh?.setEnableRefresh(false) //是否启用下拉刷新功能
        vSwipeRefresh?.setEnableLoadMore(false) //是否启用上拉加载功能
        vSwipeRefresh?.setEnablePureScrollMode(true) //是否启用纯滚动模式
        vSwipeRefresh?.setEnableOverScrollBounce(true) //是否启用越界回弹
        vSwipeRefresh?.setEnableOverScrollDrag(true) //是否启用越界拖动（仿苹果效果）1.0.4
    }

    open fun getRefreshLayoutId(): Int {
        return R.id.m_refresh_layout
    }

    override fun stopRefresh() {
        if (vSwipeRefresh != null && vSwipeRefresh!!.isRefreshing) {
            vSwipeRefresh!!.finishRefresh()
        }
    }

    override fun dismissLoadingDialog() {
        super.dismissLoadingDialog()
        stopRefresh()
    }

    override fun startRefresh() {
        if (vSwipeRefresh != null && !vSwipeRefresh!!.isRefreshing) {
            vSwipeRefresh!!.autoRefresh()
        }
        onRefreshData()
    }

    /**
     * 下拉刷新
     */
    abstract fun onRefreshData()

    open fun isRefresh(): Boolean {
        return vSwipeRefresh!!.isRefreshing
    }
}