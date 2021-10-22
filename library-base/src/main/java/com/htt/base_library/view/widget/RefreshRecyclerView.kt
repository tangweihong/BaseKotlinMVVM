package com.htt.base_library.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.test.library_base.R


class RefreshRecyclerView :SmartRefreshLayout{
    var header:MaterialHeader?=null
    var recyclerView:RecyclerView?=null

    var pageNumber=0;

    constructor(context: Context?):super(context){
        initView(context)
    }

    constructor(context: Context?,attrs:AttributeSet ):super(context,attrs){
        initView(context)
    }

    private fun initView(context: Context?){
        View.inflate(context, R.layout.layout_refresh_recyclerview,this)
        header=findViewById(R.id.layout_header)
        recyclerView=findViewById(R.id.recycler_view)

    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>){
        recyclerView?.adapter=adapter
    }

    fun setLayoutManager(layoutManager:RecyclerView.LayoutManager){
        recyclerView?.layoutManager=layoutManager
    }
}