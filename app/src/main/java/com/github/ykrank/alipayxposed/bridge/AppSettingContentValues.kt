package com.github.ykrank.alipayxposed.bridge

import android.content.ContentValues
import android.net.Uri
import com.github.ykrank.alipayxposed.app.AlipayContentProvider
import com.github.ykrank.alipayxposed.hook.HookedApp

object AppSettingContentValues {

    val Key_Enable = "enable" //boolean

    fun getTableUri(): Uri {
        return Uri.Builder()
                .scheme("content")
                .authority(AlipayContentProvider.authorities)
                .path(AlipayContentProvider.Table_Setting)
                .build()
    }

    fun isEnable(values: ContentValues): Boolean? {
        return values.getAsBoolean(Key_Enable)
    }

    fun doIfEnable(func: () -> Unit) {
        val cursor = HookedApp.app?.contentResolver?.query(AppSettingContentValues.getTableUri(),
                arrayOf(AppSettingContentValues.Key_Enable), null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst() && cursor.getInt(0) == 1) {
                func.invoke()
            }
            cursor.close()
        }
    }
}