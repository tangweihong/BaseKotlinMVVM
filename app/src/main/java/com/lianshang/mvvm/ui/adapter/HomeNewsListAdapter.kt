package com.lianshang.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lianshang.mvvm.R
import com.lianshang.mvvm.model.HomeEntity
import com.lianshang.mvvm.model.Information
import com.lianshang.mvvm.model.Market
import java.math.BigDecimal

class HomeNewsListAdapter : BaseQuickAdapter<Information, BaseViewHolder>(R.layout.item_home_news_list) {


    override fun convert(helper: BaseViewHolder, item: Information) {
        helper.setText(R.id.item_tv_content, item.title)
        helper.setText(R.id.item_tv_time, item.time.toString())

    }
}