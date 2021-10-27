package com.htt.base_library.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.htt.base_library.base.internal.IBaseListView
import com.test.library_base.R

abstract class BaseListActivity<Adapter : BaseQuickAdapter<T, *>, T, VM : BaseVModel, VB : ViewBinding> :
    BaseSwipeRefreshActivity<VM, VB>(), IBaseListView<Adapter, T> {


    protected val mAdapter: Adapter by lazy {
        setAdapterInstance()
    }

    protected val mRecyclerView: RecyclerView? by lazy {
        findViewById<RecyclerView>(getRecyclerviewId())
    }


    private var pageSize = 15
    private var pagerNumber = 1


    private fun setAdapterInstance(): Adapter =
        getAdapterClass()?.newInstance() ?: throw IllegalArgumentException("adapter == null")

    protected fun getAdapter(): Adapter = mAdapter;

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)
        initRv()
    }


    override fun showError(msg: String, code: Int) {
        super.showError(msg, code)
        if (pagerNumber != 1) {
            pagerNumber -= 1
        }
    }

    //用于子类手动刷新
    override fun startRefresh() {
        super.startRefresh()
        pagerNumber = 1
        onLoadMoreData(pagerNumber)
    }

    protected open fun initRv() {
        mRecyclerView?.let {
            it.layoutManager = newLayoutManager(this)
            it.adapter = mAdapter
        }
        setHasFixedSize()
        if (isInitLoadMoreModule()) {
            mAdapter.loadMoreModule.setOnLoadMoreListener {
                if (!isRefresh()) {
                    pagerNumber++
                    loadData()
                }
            }
            mAdapter.loadMoreModule.isAutoLoadMore = true
            mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            mAdapter.loadMoreModule.loadMoreView = object : BaseLoadMoreView() {
                override fun getRootView(viewGroup: ViewGroup): View {
                    // 整个 LoadMore 布局
                    return LayoutInflater.from(mContext)
                        .inflate(R.layout.view_load_more, viewGroup, false)
                }

                override fun getLoadingView(baseViewHolder: BaseViewHolder): View {
                    // 布局中 “加载中”的View
                    return baseViewHolder.getView(R.id.load_more_loading_view)
                }

                override fun getLoadComplete(baseViewHolder: BaseViewHolder): View {
                    // 布局中 “当前一页加载完成”的View
                    return baseViewHolder.getView(R.id.load_more_load_complete_view)
                }

                override fun getLoadEndView(baseViewHolder: BaseViewHolder): View {
                    // 布局中 “全部加载结束，没有数据”的View
                    return baseViewHolder.getView(R.id.load_more_load_end_view)
                }

                override fun getLoadFailView(baseViewHolder: BaseViewHolder): View {
                    // 布局中 “加载失败”的View
                    return baseViewHolder.getView(R.id.load_more_load_fail_view)
                }
            }
        }
    }
    /**
     * 加载更多数据
     */
    abstract fun onLoadMoreData()

    /**
     * 设置高度固定
     *
     *
     * 如果item高度是固定的话，可以使用RecyclerView.setHasFixedSize(true);来避免requestLayout浪费资源。
     */
    open fun setHasFixedSize() {
        mRecyclerView?.setHasFixedSize(true)
    }

    /**
     * 是否初始化LoadMoreModule
     *
     * @return 默认true
     */
    open fun isInitLoadMoreModule(): Boolean {
        return true
    }

    override fun onSuccessList(list: MutableList<T>?) {
        if (pagerNumber == 1) {
            mAdapter.setList(list)
        } else {
            mAdapter.addData(list ?: emptyList())
        }

        if (isInitLoadMoreModule()) {
            if (mAdapter.data.size < pageSize) {
                mAdapter.loadMoreModule.loadMoreEnd(true)
            } else {
                mAdapter.loadMoreModule.loadMoreComplete()
            }
        }
        if (mAdapter.data == null || mAdapter.data.size <= 0) {
//            setEmptyView()
        }
    }


    /**
     * get page index
     *
     * @return page index
     */
    open fun getPageIndex(): Int {
        return pagerNumber
    }

    /**
     * set the list page no
     *
     * @param pageIndex page index
     */
    open fun setPageIndex(pageIndex: Int) {
        pagerNumber = pageIndex
    }

    /**
     * get page pageSize
     *
     * @return page pageSize
     */
    open fun getPageSize(): Int {
        return pageSize
    }

    /**
     * set the list page no
     *
     * @param size page size
     */
    open fun setPageSize(size: Int) {
        pageSize = size
    }
}


