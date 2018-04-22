package com.github.ykrank.alipayxposed.app.data.db

import android.database.Cursor
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.BillDetailsRaw
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.BillDetailsRawDao
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.BillDetailsRawDao.Properties
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.DaoSession
import com.github.ykrank.androidtools.util.L
import org.jsoup.Jsoup


/**
 * 对原始账单详情数据库的操作包装
 * Created by AdminYkrank on 2016/2/23.
 */
class BillDetailsRawDbWrapper(private val appDaoSessionManager: AppDaoSessionManager) {

    private val dao: BillDetailsRawDao
        get() = session.billDetailsRawDao

    private val session: DaoSession
        get() = appDaoSessionManager.daoSession

    fun getAllList(limit: Int, offset: Int): List<BillDetailsRaw> {
        return dao.queryBuilder()
                .limit(limit)
                .offset(offset)
                .list()
    }

    fun fromCursor(cursor: Cursor): BillDetailsRaw {
        return dao.readEntity(cursor, 0)
    }

    fun getWithTradeNo(tradeNo: String): BillDetailsRaw? {
        return dao.queryBuilder()
                .where(Properties.TradeNo.eq(tradeNo))
                .list()
                .firstOrNull()
    }

    fun getCursorWithTradeNo(tradeNo: String): Cursor? {
        return dao.queryBuilder()
                .where(Properties.TradeNo.eq(tradeNo))
                .buildCursor().query()
    }

    fun save(billDetailsRaw: BillDetailsRaw) {
        val oData = getWithTradeNo(billDetailsRaw.tradeNo)
        if (oData == null) {
            dao.insert(billDetailsRaw)
        } else {
            dao.update(merge(oData, billDetailsRaw))
        }
    }

    fun merge(oData: BillDetailsRaw, nData: BillDetailsRaw): BillDetailsRaw {
        oData.id?.let { nData.id = it }
        return nData
    }

    fun del(billDetailsRaw: BillDetailsRaw) {
        val oData = getWithTradeNo(billDetailsRaw.tradeNo)
        if (oData != null) {
            dao.delete(oData)
        }
    }

    companion object {
        fun parseHtmlToRaw(tradeNo: String, html: String): BillDetailsRaw {
            val result = BillDetailsRaw()
            result.tradeNo = tradeNo
            try {
                val document = Jsoup.parse(html)
                result.header = document.getElementsByClass("header-title")[0].text()
                result.price = document.getElementsByClass("price-sum")[0].text().toFloat()
                result.status = document.getElementsByClass("price-status")[0].text()

                val rawJson = mutableMapOf<String, String>()
                document.getElementsByClass("bl-detail-common")
                        .forEach {
                            assert(it.childNodeSize() == 2)
                            val name = it.child(0).text()
                            val value = it.child(1).text()
                            rawJson[name] = value
                        }
                result.rawJson = App.app.objectMapper.writeValueAsString(rawJson)
            } catch (e: Exception) {
                result.rawHtml = html
                L.report(e)
            }
            return result
        }
    }
}
