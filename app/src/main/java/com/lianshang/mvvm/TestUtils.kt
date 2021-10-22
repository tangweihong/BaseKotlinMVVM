package com.lianshang.mvvm

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.Thread.sleep

/**
 *
 * Created by hong on 2021/7/22 10:35.
 *
 */

 fun main() {

    fun <T> getName(data: T) {

    }
    var a = 0;
    fun test(): Int {
        try {
            a = 1
            return a
        } catch (ex: Exception) {
            a = 2
        } finally {
            a = 3
            return a
        }
        return a
    }
    println(test())

//    GlobalScope.launch { // 在后台启动一个新的协程并继续
//        delayA() // 协程已在等待时主线程还在继续，
//        println("Hello2,") // 协程已在等待时主线程还在继续  源界
//        delayB()
//    }
//    println("Hello,") // 协程已在等待时主线程还在继续
//    Thread.sleep(4000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
    //秦皇古墓，沐凡，入墓，墓穴深处，人皇剑，千古谜团，震惊全球，玛雅遗址，飞机偶遇，抵达，玛雅预言，预言轮盘,
}

suspend fun delayA() {
    delay(1000L)
    println("delayA!" + System.currentTimeMillis())
}

suspend fun delayB() {
    delay(2000L)
    println("delayB!" + System.currentTimeMillis())
}