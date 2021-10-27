package com.htt.base_library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.htt.base_library.base.internal.IBaseListView
import com.test.library_base.R

abstract class BaseListFragment<Adapter : BaseQuickAdapter<T, *>, T, VM : BaseVModel, VB : ViewBinding> :
    BaseSwipeRefreshFragment<VM, VB>(), IBaseListView<Adapter, T> {
    protected val mAdapter: Adapter by lazy {
        getAdapter()
    }
    protected val mRecyclerView: RecyclerView? by lazy {
        mRootView.findViewById<RecyclerView>(getRecyclerviewId())
    }

    private var pageSize = 15

    private var pagerNumber = 1


    protected open fun getAdapter(): Adapter =
        getAdapterClass()?.newInstance() ?: throw IllegalArgumentException("adapter == null")


    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
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
            it.layoutManager = newLayoutManager(mContext)
            it.adapter = mAdapter
        }
        setHasFixedSize()
        if (isInitLoadMoreModule()) {
            mAdapter.loadMoreModule.setOnLoadMoreListener {
                if (!isRefresh()) {
                    pagerNumber++
                    onLoadMoreData(pagerNumber)
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
            setListEmptyView()
        }
    }
    protected var mEmptyLlt: LinearLayout? = null
    protected var mEmptyImage: ImageView? = null
    protected var mEmptyText: TextView? = null

    /**
     * 先判断数据，再设置空数据视图
     */
    open fun setListEmptyView() {
        if (getAdapter().data == null || getAdapter().data.size <= 0) {
            val view: View =
                LayoutInflater.from(mContext).inflate(R.layout.layout_list_empty_view, null)
            mEmptyLlt = view.findViewById<LinearLayout>(R.id.empty_llt)
            mEmptyImage = view.findViewById<ImageView>(R.id.empty_img)
            mEmptyText = view.findViewById<TextView>(R.id.empty_tv)
            mAdapter.setEmptyView(view)
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


