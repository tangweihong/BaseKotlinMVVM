package com.htt.base_library.base

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseLazyFragment<VM : BaseVModel, VB : ViewBinding> : BaseFragment<VM,VB>() {
    var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyLoad()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        isLoaded = false
        super.onDestroyView()
    }

    abstract fun lazyLoad()
}