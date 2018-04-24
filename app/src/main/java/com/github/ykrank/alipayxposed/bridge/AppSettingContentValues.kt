package com.github.ykrank.alipayxposed.bridge

import android.net.Uri
import com.github.ykrank.alipayxposed.app.AlipayContentProvider

object AppSettingContentValues {

    fun getTableUri(): Uri {
        return Uri.Builder()
                .scheme("content")
                .authority(AlipayContentProvider.authorities)
                .path(AlipayContentProvider.Table_Setting)
                .build()
    }

    fun doIfEnable(func: () -> Unit) {
        val setting = AppSetting.remoteQuery()
        if (setting?.enable == true) {
            func.invoke()
        }
    }
}