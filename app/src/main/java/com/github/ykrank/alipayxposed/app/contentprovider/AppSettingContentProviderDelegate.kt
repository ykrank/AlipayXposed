package com.github.ykrank.alipayxposed.app.contentprovider

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.app.AlipayContentProvider
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferences
import com.github.ykrank.alipayxposed.bridge.AppSetting

object AppSettingContentProviderDelegate : ContentProviderDelegate {
    private var pref: AppPreferences? = null

    override val tableName: String
        get() = AlipayContentProvider.Table_Setting

    override fun setContext(context: Context) {
        pref = App.getPref(context)
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
//        AppSettingContentValues.isEnable(values)?.let {
//            App.app.appPref.enable = it
//        }
//        return uri
        throw NotImplementedError("Could not insert setting from remote")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return pref?.let { AppSetting.fromPref(it).toCursor() }
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