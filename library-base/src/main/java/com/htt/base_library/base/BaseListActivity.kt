package com.htt.base_library.base

import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.htt.base_library.base.internal.IBaseListView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

abstract class BaseListActivity<Adapter : BaseQuickAdapter<T, *>, T, VM : BaseVModel, VB : ViewBinding> :
    BaseVMActivity<VM, VB>(), OnRefreshLoadMoreListener, IBaseListView<Adapter, T> {


    private val mAdapter: Adapter by lazy {
        setAdapterInstance()
    }

    private val mRecyclerView: RecyclerView? by lazy {
        findViewById<RecyclerView>(getRecyclerviewId())
    }

    private val mRefreshLayout: SmartRefreshLayout? by lazy {
        findViewById<SmartRefreshLayout>(getRefreshLayoutId())
    }

//    private val mStateView: MultiStateView? by lazy {
//        findViewById<MultiStateView>(getStatusViewId())
//    }


    open val pageSize = 10
    private var pagerNumber = 1


    private fun setAdapterInstance(): Adapter =
        getAdapterClass()?.newInstance() ?: throw IllegalArgumentException("adapter == null")

    protected fun getAdapter(): Adapter = mAdapter;

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)
        initRv()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) = onRefresh()
    override fun onLoadMore(refreshLayout: RefreshLayout) = onLoadMore()


    override fun showError(msg: String, code: Int) {
        super.showError(msg, code)
        onFailureList()
    }
    //用于子类手动刷新
    open fun onRefresh() {
        pagerNumber = 1
        onLoadData(true, pagerNumber)
    }

    //用于子类手动控制加载
    open fun onLoadMore() {
        pagerNumber++
        onLoadData(false, pagerNumber)
    }
    protected open fun initRv() {
        mRefreshLayout?.let {
            it.setEnableLoadMore(false)
            it.setEnableRefresh(true)
            it.setOnRefreshListener(this)
            it.setOnLoadMoreListener(this)
        }
        mRecyclerView?.let {
            it.layoutManager = newLayoutManager(this)
            it.adapter = mAdapter
        }
//        mStateView?.let {
//            it.setOnRetryListener { _ ->
//                it.viewState = MultiStateView.ViewState.LOADING
//                onRefresh()
//            }
//        }
    }


    override fun onSuccessList(list: MutableList<T>?, hasNext: Boolean) {
        if (pagerNumber == 1) {
            mAdapter.setList(list)
        } else {
            mAdapter.addData(list ?: emptyList())
        }
        mRefreshLayout?.let {
            it.finishLoadMore()
            it.finishRefresh()
            it.setNoMoreData(!hasNext)
        }
//        mStateView?.let {
//            it.viewState = if (mAdapter.itemCount > 0)
//                MultiStateView.ViewState.CONTENT else MultiStateView.ViewState.EMPTY
//        }
    }

    override fun onFailureList() {
        mRefreshLayout?.let {
            it.finishLoadMore(false)
            it.finishRefresh(false)
            it.setNoMoreData(true)
        }
//        mStateView?.let {
//            it.viewState = if (mAdapter.itemCount > 0)
//                MultiStateView.ViewState.CONTENT else MultiStateView.ViewState.ERROR
//        }
    }


}


