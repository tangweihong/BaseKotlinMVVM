package com.htt.base_library.util

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.htt.base_library.base.NoViewModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 *
 */
object ClassUtil {
    /**
     * 获取泛型ViewModel的class对象
     */
    fun <T> getViewModel(obj: Any): Class<T>? {
        val currentClass: Class<*> = obj.javaClass
        val tClass: Class<T>? = getGenericClass(currentClass, ViewModel::class.java)
        return if (tClass == null || tClass == ViewModel::class.java || tClass == NoViewModel::class.java) {
            null
        } else tClass
    }

    private fun <T> getGenericClass(
        kClass: Class<*>,
        filterClass: Class<*>
    ): Class<T>? {
        val type: Type? = kClass.genericSuperclass
        if (type == null || type !is ParameterizedType) return null
        val parameterizedType: ParameterizedType = type as ParameterizedType
        val types: Array<Type> = parameterizedType.actualTypeArguments
        for (t in types) {
            val tClass = t as Class<T>
            if (filterClass.isAssignableFrom(tClass)) {
                return tClass
            }
        }
        return null
    }

    /**
     * 获取泛型VM的实际类型
     * 如果基于BaseVM继续抽象 请确保VM泛型为泛型参数中最后一个
     */
    @Suppress("UNCHECKED_CAST")
    fun <VM> getTClass(): Class<VM> {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments.first() as Class<VM>
    }

}