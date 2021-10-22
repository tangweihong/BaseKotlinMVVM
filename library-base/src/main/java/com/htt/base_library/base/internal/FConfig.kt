package com.htt.base_library.base.internal

import com.test.library_base.R


data class FConfig(
    val layout: Int,
    val hideToolbar: Boolean = false,
    val navigationIcon: Int = R.drawable.comm_titlebar_back_normal,
    val title: String = "",
    val rightText: String = "",
    val rightImage: Int = 0,
    val supportSwipeBack: Boolean = true,
    val statusBarDarkFont: Boolean = false
)