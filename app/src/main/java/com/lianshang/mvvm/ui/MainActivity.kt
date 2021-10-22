package com.lianshang.mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ktx.immersionBar
import com.htt.base_library.base.BaseActivity
import com.htt.base_library.util.showToast
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.ActivityMainBinding
import com.lianshang.mvvm.model.TabEntity
import com.lianshang.mvvm.ui.fragment.MainHomeFragment
import com.lianshang.mvvm.ui.fragment.MainMineFragment
import com.lianshang.mvvm.ui.fragment.MainProjectFragment
import com.lianshang.mvvm.ui.fragment.MainSquareFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var fragments = arrayOfNulls<Fragment>(4)
    var tabList = arrayListOf<CustomTabEntity>()

    var currentIndex = 0

    var exitTime: Long = 0


    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        initFragments(savedInstanceState)

        initTabLayout()
    }

    override fun loadData() {

    }

    private var mHomeFragment: MainHomeFragment? = null
    private var mSquareFragment: MainSquareFragment? = null
    private var mProjectFragment: MainProjectFragment? = null
    private var mMineFragment: MainMineFragment? = null

    private val MAIN_HOME = "homeFragment"
    private val MAIN_SQUARE = "exchangeFragment"
    private val MAIN_PROJECT = "appFragment"
    private val MAIN_MINE = "mineFragment"


    override fun onSaveInstanceState(outState: Bundle) {
        /*fragment不为空时 保存*/
        if (mHomeFragment != null) {
            supportFragmentManager.putFragment(outState, MAIN_HOME, mHomeFragment!!)
        }
        if (mSquareFragment != null) {
            supportFragmentManager.putFragment(outState, MAIN_SQUARE, mSquareFragment!!)
        }
        if (mProjectFragment != null) {
            supportFragmentManager.putFragment(outState, MAIN_PROJECT, mProjectFragment!!)
        }
        if (mMineFragment != null) {
            supportFragmentManager.putFragment(outState, MAIN_MINE, mMineFragment!!)
        }
        super.onSaveInstanceState(outState)
        outState.putInt("current_tab", currentIndex)
    }

    private fun initFragments(saved: Bundle?) {
        saved?.apply {

            /*获取保存的fragment  没有的话返回null*/
            mHomeFragment =
                supportFragmentManager.getFragment(saved, MAIN_HOME) as MainHomeFragment?
            mSquareFragment =
                supportFragmentManager.getFragment(saved, MAIN_SQUARE) as MainSquareFragment?
            mProjectFragment =
                supportFragmentManager.getFragment(saved, MAIN_PROJECT) as MainProjectFragment?
            mMineFragment =
                supportFragmentManager.getFragment(saved, MAIN_MINE) as MainMineFragment?

//            fragments[0] =
//                supportFragmentManager.findFragmentByTag(MainHomeFragment::javaClass.name)
//            fragments[1] =
//                supportFragmentManager.findFragmentByTag(MainSquareFragment::javaClass.name)
//            fragments[2] =
//                supportFragmentManager.findFragmentByTag(MainProjectFragment::javaClass.name)
//            fragments[3] =
//                supportFragmentManager.findFragmentByTag(MainMineFragment::javaClass.name)
            currentIndex = saved.getInt("current_tab", 0)
        }
//        if (fragments[0] == null) fragments[0] = MainHomeFragment()
//        if (fragments[1] == null) fragments[1] = MainSquareFragment()
//        if (fragments[2] == null) fragments[2] = MainProjectFragment()
//        if (fragments[3] == null) fragments[3] = MainMineFragment()
    }

    private fun initTabLayout() {
        tabList.add(TabEntity(R.mipmap.ic_main_tab_home, R.mipmap.ic_main_tab_home_selected, "首页"))
        tabList.add(
            TabEntity(
                R.mipmap.ic_main_tab_mining,
                R.mipmap.ic_main_tab_mining_selected,
                "广场"
            )
        )
        tabList.add(
            TabEntity(
                R.mipmap.ic_main_tab_wallet,
                R.mipmap.ic_main_tab_wallet_selected,
                "项目"
            )
        )
        tabList.add(TabEntity(R.mipmap.ic_main_tab_my, R.mipmap.ic_main_tab_my_selected, "我的"))

        mViewBinding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                currentIndex = position
                selectedFragment(position)
//                var fragment = fragments[position]
//                fragment?.also {
//                    currentIndex = position
//                    selectedFragment(position)
////                    showHideFragment(it)
//                }
                //修改状态栏文字颜色
                when (position) {
                    0, 3 -> {
                        immersionBar {
                            statusBarDarkFont(true, 0.2f)
                                .init()
                        }
                    }
                    1, 2 -> {
                        immersionBar {
                            statusBarDarkFont(true, 0.2f)
                                .init()
                        }
                    }
                }
            }

            override fun onTabReselect(position: Int) {
            }
        })

        mViewBinding.tabLayout.setTabData(tabList)
        mViewBinding.tabLayout.currentTab = currentIndex
        LogUtils.d("fragment_size：${fragments.size}")
        selectedFragment(currentIndex)
//        loadFragments(R.id.layout_main_container, currentIndex, *fragments.requireNoNulls())
    }


    private fun selectedFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        when (position) {
            0 -> {
                if (mHomeFragment == null) {
                    mHomeFragment = MainHomeFragment()
                    transaction.add(R.id.layout_main_container, mHomeFragment!!)
                } else {
                    transaction.show(mHomeFragment!!)
                }
                transaction.setMaxLifecycle(mHomeFragment!!, Lifecycle.State.RESUMED)
            }
            1 -> {
                if (mSquareFragment == null) {
                    mSquareFragment = MainSquareFragment()
                    transaction.add(R.id.layout_main_container, mSquareFragment!!)
                } else {
                    transaction.show(mSquareFragment!!)
                }
                transaction.setMaxLifecycle(mSquareFragment!!, Lifecycle.State.RESUMED)
            }
            2 -> {
                if (mProjectFragment == null) {
                    mProjectFragment = MainProjectFragment()
                    transaction.add(R.id.layout_main_container, mProjectFragment!!)
                } else {
                    transaction.show(mProjectFragment!!)
                }
                transaction.setMaxLifecycle(mProjectFragment!!, Lifecycle.State.RESUMED)
            }
            3 -> {
                if (mMineFragment == null) {
                    mMineFragment = MainMineFragment()
                    transaction.add(R.id.layout_main_container, mMineFragment!!)
                } else {
                    transaction.show(mMineFragment!!)
                }
                transaction.setMaxLifecycle(mMineFragment!!, Lifecycle.State.RESUMED)
            }
            else -> {
            }
        }
        transaction.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment!!)
            transaction.setMaxLifecycle(mHomeFragment!!, Lifecycle.State.STARTED)
        }
        if (mSquareFragment != null) {
            transaction.hide(mSquareFragment!!)
            transaction.setMaxLifecycle(mSquareFragment!!, Lifecycle.State.STARTED)
        }
        if (mProjectFragment != null) {
            transaction.hide(mProjectFragment!!)
            transaction.setMaxLifecycle(mProjectFragment!!, Lifecycle.State.STARTED)
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment!!)
            transaction.setMaxLifecycle(mMineFragment!!, Lifecycle.State.STARTED)
        }
    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                //ToastUtils.showShort(R.string.exit_app)
                showToast(StringUtils.getString(R.string.press_again_to_exit_the_program))
                exitTime = System.currentTimeMillis()
            } else {
                Process.killProcess(Process.myPid())
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}