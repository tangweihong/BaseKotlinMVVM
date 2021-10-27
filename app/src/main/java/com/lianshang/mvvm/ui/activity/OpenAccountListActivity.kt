package com.lianshang.mvvm.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.SizeUtils
import com.htt.base_library.base.BaseListActivity
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.ActivityOpenAccountListBinding
import com.lianshang.mvvm.model.Data
import com.lianshang.mvvm.ui.adapter.OpenAccountListAdapter
import com.lianshang.mvvm.ui.viewmodel.OpenAccountModel

class OpenAccountListActivity :
    BaseListActivity<OpenAccountListAdapter, Data, OpenAccountModel, ActivityOpenAccountListBinding>() {

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)

        viewModel.openAccountList.observe(this) {
            onSuccessList(it.data)
        }
    }

    override fun getTitleBar(): View? {
        return mViewBinding.titleBar.also {
            it.leftTextView.setPadding(
                SizeUtils.dp2px(20f),
                it.leftTextView.paddingTop,
                it.leftTextView.paddingRight,
                it.leftTextView.paddingBottom
            )
            ClickUtils.applyGlobalDebouncing(it.leftTextView) {
                finish()
            }
        }
    }

    override fun isInitLoadMoreModule(): Boolean {
        return false
    }
    override fun loadData() {
        viewModel.getMainData()
    }

    override fun onLoadData(pagerNumber: Int) {
        viewModel.getMainData()
    }
}