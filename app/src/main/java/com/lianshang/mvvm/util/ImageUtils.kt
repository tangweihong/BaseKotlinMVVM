package com.lianshang.mvvm.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lianshang.mvvm.R
import com.lianshang.mvvm.ext.dp

fun <T : BaseViewHolder> T.loadUrl(idRes: Int, imageUrl: String?) = apply {
    getView<ImageView>(idRes).loadUrl(imageUrl)
}

fun <T : BaseViewHolder> T.loadDrawableRes(idRes: Int, @DrawableRes res: Int) = apply {
    getView<ImageView>(idRes).setImageResource(res)
}

fun <T : BaseViewHolder> T.loadHeadImage(idRes: Int, imageUrl: String?) = apply {
    getView<ImageView>(idRes).loadHeadImage(imageUrl)
}

fun <T : BaseViewHolder> T.loadBannerImage(idRes: Int, imageUrl: String?, radius: Int = 5) = apply {
    getView<ImageView>(idRes).loadBannerImage(imageUrl, radius)
    return this
}

fun ImageView.loadGif(imageUrl: Int?) {
    Glide.with(context)
        .asGif()
        .load(imageUrl)
        .into(this)
}

fun ImageView.loadUrl(imageUrl: String?, defaultResId: Int = R.drawable.img_default) {
    Glide.with(context)
        .load(imageUrl)
        .apply(getRequestOptions(defaultResId))
        .into(this)
}

fun ImageView.loadHeadImage(imageUrl: String?, defaultResId: Int = R.drawable.img_head) {
    Glide.with(context)
        .load(imageUrl)
        .apply(getRequestOptions(defaultResId).circleCrop())
        .into(this)
}

fun ImageView.loadBannerImage(
    imageUrl: String?,
    radius: Int = 5,
    defaultResId: Int = R.drawable.bg_banner_default
) {
    val transform = RoundedCorners(radius.dp)
    Glide.with(context)
        .load(imageUrl)
        .apply(getRequestOptions(defaultResId).transform(transform))
        .into(this)
}

private fun getRequestOptions(defaultResId: Int) = RequestOptions()
    .error(defaultResId)
    .placeholder(defaultResId)
    .fallback(defaultResId)
