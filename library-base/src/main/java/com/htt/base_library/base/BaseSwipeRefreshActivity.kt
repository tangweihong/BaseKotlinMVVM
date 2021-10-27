package com.htt.base_library.base

import android.content.Intent
import android.os.Bundle
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
abstract class BaseSwipeRefreshActivity<VM : BaseVModel, VB : ViewBinding> :
    BaseVMActivity<VM, VB>(), ISwipeRefreshView {

    open val vSwipeRefresh: SmartRefreshLayout? by lazy {
        findViewById<SmartRefreshLayout>(getRefreshLayoutId())
    }

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)
        vSwipeRefresh?.setEnableLoadMore(false) //是否启用上拉加载功能
        vSwipeRefresh?.setRefreshHeader(setRefreshHeader())
        vSwipeRefresh?.setOnRefreshListener(OnRefreshListener { startRefresh() })
    }

    open fun setRefreshHeader(): RefreshHeader {
        return MaterialHeader(this)
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

    open fun getRefreshLayoutId() = R.id.m_refresh_layout

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