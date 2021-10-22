package com.htt.base_library.base

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.htt.base_library.base.internal.IBaseListView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

abstract class BaseListFragment<Adapter : BaseQuickAdapter<T, *>, T, VM : BaseVModel, VB : ViewBinding> :
    BaseLazyFragment<VM, VB>(), OnRefreshListener, IBaseListView<Adapter, T> {
    protected val mAdapter: Adapter by lazy {
        getAdapter()
    }
    protected val mRecyclerView: RecyclerView? by lazy {
        mRootView.findViewById<RecyclerView>(getRecyclerviewId())
    }


    private val mRefreshLayout: SmartRefreshLayout? by lazy {
        mRootView.findViewById<SmartRefreshLayout>(getRefreshLayoutId())
    }


    open val pageSize = 10
    private var pagerNumber = 1


    protected open fun getAdapter(): Adapter =
        getAdapterClass()?.newInstance() ?: throw IllegalArgumentException("adapter == null")


    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        initRv()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) = onRefresh()


    //用于子类手动刷新
    open fun onRefresh() {
        pagerNumber = 1
        onLoadData(true, pagerNumber)
    }

//    //用于子类手动控制加载
//    open fun onLoadMore() {
//        pagerNumber++
//        onLoadData(false, pagerNumber)
//    }

    protected open fun initRv() {
        mRefreshLayout?.let {
            it.setOnRefreshListener(this)
            it.setEnableRefresh(true) //是否启用下拉刷新功能
            it.setEnableLoadMore(false) //是否启用上拉加载功能
            it.setEnablePureScrollMode(false) //是否启用纯滚动模式
            it.setEnableOverScrollBounce(true) //是否启用越界回弹
            it.setEnableOverScrollDrag(true) //是否启用越界拖动（仿苹果效果）1.0.4
        }
        mRecyclerView?.let {
            it.layoutManager = newLayoutManager(mContext)
            it.adapter = mAdapter
        }
    }

    override fun onSuccessList(list: MutableList<T>?, hasNext: Boolean) {
        if (pagerNumber == 1) {
            mAdapter.setList(list)
        } else {
            mAdapter.addData(list ?: emptyList())
        }
        mRefreshLayout?.let {
            it.finishRefresh()
        }
    }

    override fun onFailureList() {
        mRefreshLayout?.let {
            it.finishRefresh(false)
        }
    }
}


