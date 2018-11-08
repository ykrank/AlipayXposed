package com.github.ykrank.alipayxposed.app

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.github.ykrank.alipayxposed.BuildConfig
import com.github.ykrank.alipayxposed.app.contentprovider.BillContentProviderDelegate
import com.github.ykrank.alipayxposed.app.contentprovider.ContentProviderDelegate
import com.github.ykrank.alipayxposed.app.contentprovider.AppSettingContentProviderDelegate
import java.util.*

class AlipayContentProvider : ContentProvider() {

    private val delegates = listOf(BillContentProviderDelegate, AppSettingContentProviderDelegate)

    /**
     * uri code, ContentProviderDelegate, is item
     */
    private val codeDelegateMap = hashMapOf<Int, Pair<ContentProviderDelegate, Boolean>>()

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        delegates.forEach {
            addURI(authorities, it.tableName, code)
            codeDelegateMap[code] = Pair(it, false)
            code++
            addURI(authorities, "${it.tableName}/#", code)
            codeDelegateMap[code] = Pair(it, true)
            code++
        }
    }

    override fun onCreate(): Boolean {
        delegates.forEach {
            it.setContext(context!!)
        }
        return true
    }

    override fun getType(uri: Uri): String? {
        val code = sUriMatcher.match(uri)
        val codeDelegate = codeDelegateMap[code]
        if (codeDelegate != null) {
            return if (codeDelegate.second) {
                "vnd.android.cursor.item/vnd.$authorities.${codeDelegate.first.tableName}"
            } else {
                "vnd.android.cursor.dir/vnd.$authorities.${codeDelegate.first.tableName}"
            }
        }
        return null
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val code = sUriMatcher.match(uri)
        val nUri = codeDelegateMap[code]?.first?.insert(uri, values)
        if (nUri != null) {
            context.contentResolver.notifyChange(nUri, null)
        }
        return nUri
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val code = sUriMatcher.match(uri)
        return codeDelegateMap[code]?.first?.query(uri, projection, selection, selectionArgs, sortOrder)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        val code = sUriMatcher.match(uri)
        val count = codeDelegateMap[code]?.first?.update(uri, values, selection, selectionArgs) ?: 0
        if (count > 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val code = sUriMatcher.match(uri)
        val count = codeDelegateMap[code]?.first?.delete(uri, selection, selectionArgs) ?: 0
        if (count > 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return count
    }

    //可以使用事务优化
    override fun applyBatch(operations: ArrayList<ContentProviderOperation>?): Array<ContentProviderResult> {
        return super.applyBatch(operations)
    }

    companion object {
        val authorities = "${BuildConfig.APPLICATION_ID}.alipayprovider"
        //账单详情
        val Table_BILL_H5 = "bill_h5"
        //设置
        val Table_Setting = "setting"

        var code = 1
    }
}

