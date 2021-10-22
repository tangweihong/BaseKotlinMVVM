package com.lianshang.mvvm.model

import com.stx.xhb.androidx.entity.BaseBannerInfo


/**
 *
 * Created by hong on 2021/7/3 14:23.
 *
 */
data class HomeEntity(
    val article: MutableList<Article>,
    val banner: MutableList<Banner>,
    val information: MutableList<Information>,
    val market: MutableList<Market>
)

data class Article(
    val en_introduction: String,
    val en_title: String,
    val id: Int,
    val introduction: String,
    val title: String
) {
    override fun toString(): String = title ?: ""
}

data class Banner(
    val created_at: String,
    val en_image: String,
    val id: Int,
    val img: String,
    val name: String,
    val status: String,
    val url: String,
    val zh_image: String
) : BaseBannerInfo {
    override fun getXBannerUrl(): String = img ?: ""

    override fun getXBannerTitle(): String = name ?: ""
}

data class Information(
    val content: String,
    val created_at: String,
    val id: Int,
    val images: List<Image>,
    val time: Int,
    val title: String
)

data class Market(
    val block: String,
    val buy_fee: String,
    val cny_precision: Int,
    val id: Int,
    val is_push: Int,
    val is_trade: Int,
    val max_buy: String,
    val max_sell: String,
    val min_buy: String,
    val min_buy_amount: String,
    val min_sell: String,
    val min_sell_amount: String,
    val name: String,
    val number_precision: Int,
    val price: String,
    val price_cny: String,
    val price_precision: Int,
    val scope: String,
    val sell_fee: String,
    val status: Int,
    val symbol: String
)

data class Image(
    val height: Int,
    val thumbnail: String,
    val url: String,
    val width: Int
)