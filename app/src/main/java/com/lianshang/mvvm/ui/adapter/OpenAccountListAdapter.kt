package com.lianshang.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lianshang.mvvm.R
import com.lianshang.mvvm.model.Data
import com.lianshang.mvvm.model.HomeEntity
import com.lianshang.mvvm.model.Market
import java.math.BigDecimal

class OpenAccountListAdapter : BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item_home_market_list) {

    override fun convert(helper: BaseViewHolder, item: Data) {

    }
}