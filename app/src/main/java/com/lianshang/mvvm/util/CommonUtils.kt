package com.lianshang.mvvm.util

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.SDCardUtils
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

object CommonUtils {
    fun validatePwd(pwd: String):Boolean{
//        var regex1=Regex("[\\da-zA-Z]{6,20}")
//        var regex2=Regex(".*\\d.*")
//        var regex3=Regex(".*[a-zA-Z].*")
        var regex=Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,20}\$")
        //return pwd.matches(regex1)&&pwd.matches(regex2)&&pwd.matches(regex3)
        return pwd.matches(regex)
    }

    fun formatNumber(number: Long, halfUp: Boolean=false): String {
        val formater = DecimalFormat()
        // keep 2 decimal places
        formater.maximumFractionDigits = 2
        formater.groupingSize = 3
        formater.roundingMode = if (halfUp) RoundingMode.HALF_UP else RoundingMode.FLOOR
        return formater.format(number)
    }

    fun getHtmlData(bodyHTML: String?): String {
        LogUtils.d("bodyHtml：$bodyHTML")
        var html=bodyHTML?.let {
            it.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&copy;", "©")

        }?:"暂无内容"
        LogUtils.d("html：$html")
        return if(html.endsWith("</html>")){
            html
        }else {
            val head = """<head>
                           <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                           <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no"> 
                           <style>body{margin: 0;padding:0;}img{max-width: 100%; width:auto; height:auto;}</style>
                       </head>"""
            "<html>$head<body style=\"text-align: justify;word-break: break-all;line-height:25px;background-color:#ffffff;margin-left:25px;margin-right:25px;\">$html</body></html>"
        }
    }

    fun getAppDownloadPath():String{
        return if(SDCardUtils.isSDCardEnableByEnvironment()){
            PathUtils.getExternalDownloadsPath()+ File.separator+AppUtils.getAppName()+".apk"
        }else{
            PathUtils.getDownloadCachePath()+ File.separator+AppUtils.getAppName()+".apk"
        }
    }
}