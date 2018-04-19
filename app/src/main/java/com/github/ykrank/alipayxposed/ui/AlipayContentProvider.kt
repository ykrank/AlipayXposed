package com.github.ykrank.alipayxposed.ui

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.github.ykrank.alipayxposed.bridge.BillH5ContentValues
import com.github.ykrank.androidtools.util.L
import java.util.ArrayList

class AlipayContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/vnd.$authorities.$Table_BILL_H5"
            2 -> "vnd.android.cursor.item/vnd.$authorities.$Table_BILL_H5"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val tradeNo = BillH5ContentValues.getTradeNo(values)
        val nUri = uri.buildUpon().fragment(tradeNo).build()
        L.d("uri:$uri, nUri:$nUri")
        context.contentResolver.notifyChange(nUri, null)
        return nUri
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        TODO("Implement this to handle query requests from clients.")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    //可以使用事务优化
    override fun applyBatch(operations: ArrayList<ContentProviderOperation>?): Array<ContentProviderResult> {
        return super.applyBatch(operations)
    }

    companion object {
        val authorities = "com.github.ykrank.alipayxposed.provider"
        val Table_BILL_H5 = "bill_h5"

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            //bill 表
            addURI(authorities, Table_BILL_H5, 1)
            //bill 表 某一行
            addURI(authorities, "$Table_BILL_H5/#", 2)
        }

    }
}
