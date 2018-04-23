package com.github.ykrank.alipayxposed.app.business

import com.fasterxml.jackson.core.type.TypeReference
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.BillDetailsRaw


class BillDetail {
    /**
     * 交易订单ID
     */
    var tradeNo: String? = null
    /**
     * 交易订单标题，一般为交易方名称
     */
    var header: String? = null
    /**
     * 交易订单金额
     */
    var price: String? = null
    /**
     * 交易订单状态
     */
    var status: Int = 0
    /**
     * 商品说明
     */
    var explain: String? = null

    companion object {
        const val Status_Success = "交易成功" //1

        fun parseFromRawJson(raw: BillDetailsRaw): BillDetail {
            if (!raw.rawHtml.isNullOrEmpty()) {
                throw IllegalStateException("RawHtml is not null, parse html to json error : $raw")
            }
            val billDetail = BillDetail()
            billDetail.tradeNo = raw.tradeNo
            billDetail.header = raw.header
            billDetail.price = raw.price
            billDetail.status = when (raw.status) {
                Status_Success -> 1
                else -> throw IllegalStateException("Unknown bill status: ${raw.status}")
            }
            checkNotNull(raw.rawJson, { "Raw json is null" })
            val map = App.app.objectMapper.readValue<Map<String, String>>(raw.rawJson, object : TypeReference<Map<String, String>>() {})
            map.forEach { t, u ->
                TODO("已知的类型")
                when (t) {
                    "商品说明" -> billDetail.explain = u
                    else -> throw IllegalStateException("Unknown rawJson name: $t")
                }
            }
            return billDetail
        }
    }
}