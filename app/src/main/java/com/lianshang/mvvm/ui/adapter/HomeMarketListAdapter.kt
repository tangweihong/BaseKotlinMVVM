package com.lianshang.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lianshang.mvvm.R
import com.lianshang.mvvm.model.HomeEntity
import com.lianshang.mvvm.model.Market
import java.math.BigDecimal

class HomeMarketListAdapter : BaseQuickAdapter<Market, BaseViewHolder>(R.layout.item_home_market_list) {


    override fun convert(helper: BaseViewHolder, item: Market) {
        helper.setText(R.id.item_tv_symbol, item.symbol)
        var a = BigDecimal("1")
        helper.setText(
            R.id.item_tv_price,
            BigDecimal(item.price).setScale(item.price_precision, BigDecimal.ROUND_DOWN)
                .toPlainString()
        )
        val limit = BigDecimal(item.scope ?: "0.00");
        if (limit > BigDecimal.ZERO) {
            helper.setBackgroundResource(R.id.item_tv_float, R.drawable.shape_rectangle_ups_5)
            helper.setText(
                R.id.item_tv_float,
                "+" + limit.setScale(2, BigDecimal.ROUND_DOWN) + "%"
            )
        } else if (limit < BigDecimal.ZERO) {
            helper.setBackgroundResource(R.id.item_tv_float, R.drawable.shape_rectangle_downs_5)
            helper.setText(
                R.id.item_tv_float,
                limit.setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "%"
            )
        } else {
            helper.setBackgroundResource(R.id.item_tv_float, R.drawable.shape_rectangle_zero_5)
            helper.setText(
                R.id.item_tv_float,
                limit.setScale(2, BigDecimal.ROUND_DOWN).toPlainString().toString() + "%"
            )
        }
    }
}