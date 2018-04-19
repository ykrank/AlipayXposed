package com.github.ykrank.alipayxposed.bridge

import android.content.ContentValues
import android.net.Uri
import com.github.ykrank.alipayxposed.app.AlipayContentProvider

object BillH5ContentValues {

    val ARG_TRADE_NO = "trade_no"
    val ARG_CONTENT = "content"

    fun createContentValues(tradeNo: String?, content: String?): ContentValues {
        return ContentValues().apply {
            put(ARG_TRADE_NO, tradeNo)
            put(ARG_CONTENT, content)
        }
    }

    fun getTableUri(): Uri{
        return Uri.Builder()
                .scheme("content")
                .authority(AlipayContentProvider.authorities)
                .path(AlipayContentProvider.Table_BILL_H5)
                .build()
    }

    fun getTradeNo(values: ContentValues): String? {
        return values.getAsString(ARG_TRADE_NO)
    }

    fun getContent(values: ContentValues): String? {
        return values.getAsString(ARG_CONTENT)
    }
}