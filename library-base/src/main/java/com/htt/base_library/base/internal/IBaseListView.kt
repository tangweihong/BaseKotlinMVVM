package com.htt.base_library.base.internal

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.test.library_base.R
import java.lang.reflect.ParameterizedType

/**
 * Created by gongziyi on 2021/5/14 10:03.
 * BaseListView 接口文档
 * 用于保证 Activity/Fragment 接口名或功能一致
 */
interface IBaseListView<Adapter : BaseQuickAdapter<T, *>, T> {

    /**
     * @param isRefresh 是否为刷新
     * @param pagerNumber 页码
     */
    fun onLoadData(isRefresh: Boolean, pagerNumber: Int)

    /***/
    fun newLayoutManager(context: Context): RecyclerView.LayoutManager = LinearLayoutManager(context)
    /***/
    fun getRefreshLayoutId(): Int = R.id.m_refresh_layout
    /***/
    fun getStatusViewId(): Int = R.id.m_multiple_status_layout
    /***/
    fun getRecyclerviewId(): Int = R.id.m_recycler_view

    /**
     * 加载成功并加载数据
     * @param list 数据源
     * @param hasNext 是否有下页
     */
    fun onSuccessList(list: MutableList<T>?, hasNext: Boolean)

    /**
     * 加载失败
     */
    fun onFailureList()


    @Suppress("UNCHECKED_CAST")
    fun getAdapterClass(): Class<Adapter>? {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val typeArguments = parameterizedType.actualTypeArguments
        return typeArguments.first() as? Class<Adapter>
    }
}