package com.lianshang.mvvm.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.SizeUtils
import com.htt.base_library.base.BaseListActivity
import com.lianshang.mvvm.databinding.ActivityNewsBinding
import com.lianshang.mvvm.databinding.ActivityOpenAccountListBinding
import com.lianshang.mvvm.model.Data
import com.lianshang.mvvm.model.NewsData
import com.lianshang.mvvm.ui.adapter.NewsListAdapter
import com.lianshang.mvvm.ui.adapter.OpenAccountListAdapter
import com.lianshang.mvvm.ui.viewmodel.NewsListModel
import com.lianshang.mvvm.ui.viewmodel.OpenAccountModel
import com.wuyr.activitymessenger.startActivity

class NewsActivity :
    BaseListActivity<NewsListAdapter, NewsData, NewsListModel, ActivityNewsBinding>() {

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)
        initImmersionBar()
        viewModel.getNewsList(1,true)
    }
    override fun setEventListener() {
        getAdapter().setOnItemClickListener { _, _, position ->
            startActivity<NewsDetailsActivity>("id" to getAdapter().data[position].id)
        }
    }
    override fun loadData() {
        viewModel.newsList.observe(this) {
            onSuccessList(it.data, false)
        }
    }
    override fun onLoadData(isRefresh: Boolean, pagerNumber: Int) {
        viewModel.getNewsList(pagerNumber,false)
    }
}