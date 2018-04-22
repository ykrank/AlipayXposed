package com.github.ykrank.alipayxposed.app.contentprovider

import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.app.AlipayContentProvider
import com.github.ykrank.alipayxposed.bridge.AppSettingContentValues

object AppSettingContentProviderDelegate : ContentProviderDelegate {
    override val tableName: String
        get() = AlipayContentProvider.Table_Setting

    override fun insert(uri: Uri, values: ContentValues): Uri? {
//        AppSettingContentValues.isEnable(values)?.let {
//            App.app.appPref.enable = it
//        }
//        return uri
        throw NotImplementedError("Could not insert setting from remote")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        if (projection?.get(0) == AppSettingContentValues.Key_Enable) {
            return MatrixCursor(arrayOf(AppSettingContentValues.Key_Enable)).apply {
                addRow(arrayOf(App.app.appPref.enable))
            }
        }
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
//        if (values != null) {
//            AppSettingContentValues.isEnable(values)?.let {
//                App.app.appPref.enable = it
//                return 1
//            }
//        }
//        return 0
        throw NotImplementedError("Could not update setting from remote")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw NotImplementedError("Could not delete setting from remote")
    }
}