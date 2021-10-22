package com.lianshang.mvvm.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gyf.immersionbar.ktx.immersionBar
import com.htt.base_library.base.BaseListFragment
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.FragmentHomeBinding
import com.lianshang.mvvm.ext.clickDelay
import com.lianshang.mvvm.ext.dp
import com.lianshang.mvvm.model.Article
import com.lianshang.mvvm.model.Market
import com.lianshang.mvvm.ui.activity.NewsDetailsActivity
import com.lianshang.mvvm.ui.activity.OpenAccountListActivity
import com.lianshang.mvvm.ui.adapter.HomeMarketListAdapter
import com.lianshang.mvvm.ui.adapter.HomeNewsListAdapter
import com.lianshang.mvvm.ui.viewmodel.MainViewModel
import com.lianshang.mvvm.util.NoticeUtils
import com.lianshang.mvvm.util.loadBannerImage
import com.stx.xhb.androidx.entity.BaseBannerInfo
import com.wuyr.activitymessenger.startActivity

class MainHomeFragment :
    BaseListFragment<HomeMarketListAdapter, Market, MainViewModel, FragmentHomeBinding>() {
    private var mNoticeList: MutableList<Article>? = null
    private lateinit var mHomeNewsAdapter: HomeNewsListAdapter

    private val mNoticeUtils by lazy {
        NoticeUtils(mContext) {
            //滚动公告栏点击
            mNoticeList?.getOrNull(it)?.let { position ->
                startActivity<NewsDetailsActivity>("id" to position)
            }
        }
    }

    override fun loadData() {
        viewModel.mainData.observe(this) {
            mViewBinding.homeBanner.setBannerData(it.banner)
            onSuccessList(it.market, false)
            mHomeNewsAdapter.setList(it.information)
            mNoticeList = it.article
            if (it.article.isNullOrEmpty()) {
                mNoticeUtils.stopNotice(mViewBinding.homeMarqueeView, true);
            } else {
                mNoticeUtils.startNotice(mNoticeList, mViewBinding.homeMarqueeView);
            }
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        immersionBar {
            titleBar(mViewBinding.toolbar)
            statusBarDarkFont(true, 0.2f)
        }
        initRecyclerView()
        mViewBinding.homeBanner.loadImage { _, model, bannerView, _ ->
            (bannerView as? ImageView)?.let {
                if (model is BaseBannerInfo) {
                    it.loadBannerImage(model.xBannerUrl as String, 5)
                }
            }
        }
    }

    private fun initRecyclerView() {
        val mLayoutManager = LinearLayoutManager(mContext)
        mViewBinding.homeNewRv.layoutManager = mLayoutManager
        mHomeNewsAdapter = HomeNewsListAdapter()
        mViewBinding.homeNewRv.adapter = mHomeNewsAdapter
    }

    override fun setEventListener() {
        mViewBinding.homeTvBindApi.clickDelay {
            startActivity<NewsDetailsActivity>()
        }
        mViewBinding.homeTvOpenAccount.clickDelay {
            startActivity<OpenAccountListActivity>()
        }
    }


    override fun lazyLoad() {
        viewModel.getMainData(true)
    }

    override fun onLoadData(isRefresh: Boolean, pagerNumber: Int) {
        viewModel.getMainData(false)
    }

    override fun onResume() {
        super.onResume()
        LogUtils.e("onResume")
        mNoticeUtils.startNotice(null, mViewBinding.homeMarqueeView)
    }

    override fun onPause() {
        super.onPause()
        mNoticeUtils.stopNotice(mViewBinding.homeMarqueeView, false)
    }

}