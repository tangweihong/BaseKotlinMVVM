package com.lianshang.mvvm.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.SizeUtils
import com.htt.base_library.base.BaseListActivity
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.ActivityNewsBinding
import com.lianshang.mvvm.databinding.ActivityOpenAccountListBinding
import com.lianshang.mvvm.model.Data
import com.lianshang.mvvm.model.NewsData
import com.lianshang.mvvm.ui.adapter.NewsListAdapter
import com.lianshang.mvvm.ui.adapter.OpenAccountListAdapter
import com.lianshang.mvvm.ui.viewmodel.NewsListModel
import com.lianshang.mvvm.ui.viewmodel.OpenAccountModel
import com.wuyr.activitymessenger.startActivity

class NewsDetailsActivity :
    BaseListActivity<NewsListAdapter, NewsData, NewsListModel, ActivityNewsBinding>() {

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)
        initImmersionBar()
        getAdapter().setOnItemClickListener { _, _, _ ->
            onSuccessList(null)
        }
    }

    override fun getTitleBar(): View? {
        return mViewBinding.includeToolbar?.let {
            ClickUtils.applyGlobalDebouncing(it.toolbarImgBack) {
                finish()
            }
            it.toolbarTvTitle.text = "交易行情"
            it.toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))
            it.toolbar
        }

    }

    override fun loadData() {
        viewModel.getNewsList(1, true)
    }

    override fun setEventListener() {
        super.setEventListener()
        viewModel.newsList.observe(this) {
            onSuccessList(it.data)
        }
    }

    override fun onLoadData(pagerNumber: Int) {
        viewModel.getNewsList(pagerNumber, false)
    }
}