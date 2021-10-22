package com.lianshang.mvvm.model

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntity: CustomTabEntity {
    var title: String =""
    var selectedIcon = 0
    var unSelectedIcon = 0

    constructor(title:String){
        this.title=title
    }
    constructor(unSelectedIcon:Int,selectedIcon:Int,title:String){
        this.title=title
        this.selectedIcon=selectedIcon
        this.unSelectedIcon=unSelectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabTitle(): String {
        return title
    }
}