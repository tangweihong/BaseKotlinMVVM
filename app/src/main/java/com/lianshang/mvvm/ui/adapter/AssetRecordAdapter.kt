package com.lianshang.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lianshang.mvvm.R

class AssetRecordAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_test_list),LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.item_tv_name, item)
    }
}