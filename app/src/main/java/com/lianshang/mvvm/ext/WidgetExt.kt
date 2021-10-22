package com.lianshang.mvvm.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.stx.xhb.androidx.XBanner
import com.stx.xhb.androidx.entity.BaseBannerInfo
import com.sunfusheng.marqueeview.MarqueeView

fun XBanner.setBannerList(list: MutableList<out BaseBannerInfo>?) {
    list?.also {
        setBannerData(list)

    }
}

fun MarqueeView<*>.setMarqueeData(msgList: List<*>?) {
    msgList?.also {
        var msgs = msgList.map { item ->
            var str = item.toString()
            str
        }
        this.startWithList(msgs as List<Nothing>?)
        this.setOnItemClickListener { position, textView ->
            var item = it[position]
        }
    }
}

fun ImageView.setNetUrl(url: String?) {
    url?.takeIf { it.startsWith("http://") || it.startsWith("https://") }
        ?.also {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(this)
        }
}

fun SmartRefreshLayout.setCanLoadMore(canLoadMore: Boolean) {
    if (this.isRefreshing) finishRefresh()
    if (this.isLoading) finishLoadMore()

    if (canLoadMore) {
        this.resetNoMoreData()
    } else {
        this.finishLoadMoreWithNoMoreData()
    }
}