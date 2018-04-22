package com.github.ykrank.alipayxposed.bridge

import android.content.ContentValues
import android.net.Uri
import com.github.ykrank.alipayxposed.app.AlipayContentProvider

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

}