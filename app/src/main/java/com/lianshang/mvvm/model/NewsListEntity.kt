package com.lianshang.mvvm.model

/**
 *
 * Created by hong on 2021/9/30 11:21.
 *
 */
data class NewsListEntity(
    val current_page: Int,
    val data: MutableList<NewsData>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class NewsData(
    val title: String,
    val content: String,
    val id: String,
)