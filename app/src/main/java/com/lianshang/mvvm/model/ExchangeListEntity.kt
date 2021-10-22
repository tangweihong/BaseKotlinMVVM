package com.lianshang.mvvm.model

/**
 *
 * Created by hong on 2021/9/30 11:21.
 *
 */
data class ExchangeListEntity(
    val current_page: Int,
    val data: MutableList<Data>,
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

data class Data(
    val icon: String,
    val id: Int,
    val is_open: Int,
    val mark: String,
    val name: String,
    val promote: String,
    val status: String,
    val url: String
)