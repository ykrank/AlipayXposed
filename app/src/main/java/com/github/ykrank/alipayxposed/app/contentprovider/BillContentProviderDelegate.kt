package com.github.ykrank.alipayxposed.app.contentprovider

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.app.AlipayContentProvider
import com.github.ykrank.alipayxposed.app.data.db.BillDetailsRawDbWrapper
import com.github.ykrank.alipayxposed.bridge.AppSettingContentValues
import com.github.ykrank.alipayxposed.bridge.BillH5ContentValues
import com.github.ykrank.androidtools.util.L

object BillContentProviderDelegate : ContentProviderDelegate {

    override val tableName: String
        get() = AlipayContentProvider.Table_BILL_H5

    override fun setContext(context: Context) {

    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val tradeNo = BillH5ContentValues.getTradeNo(values)
        val content = BillH5ContentValues.getContent(values)
        val nUri = uri.buildUpon().fragment(tradeNo).build()

        if (!tradeNo.isNullOrEmpty() && !content.isNullOrEmpty()) {
            val billDetailsRaw = BillDetailsRawDbWrapper.parseHtmlToRaw(tradeNo!!, content!!)
            L.d("$billDetailsRaw")
            App.app.billDb.save(billDetailsRaw)
        }
        return nUri
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val tradeNo = uri.fragment
        if (tradeNo != null) {
            return App.app.billDb.getCursorWithTradeNo(tradeNo)
        }
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw NotImplementedError("Could not update bill db")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw NotImplementedError("Could not delete bill db")
    }
}