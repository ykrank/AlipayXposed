package com.github.ykrank.alipayxposed.app.business

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.BillDetailsRaw


@JsonIgnoreProperties(ignoreUnknown = false)
class BillDetail {
    /**
     * 交易订单ID
     */
    @JsonIgnore
    var tradeNo: String? = null
    /**
     * 交易订单标题，一般为交易方名称
     */
    @JsonIgnore
    var header: String? = null
    /**
     * 交易订单金额
     */
    @JsonIgnore
    var price: String? = null
    /**
     * 交易订单状态
     */
    @JsonIgnore
    var status: Status? = null

    @JsonProperty("付款方式")
    var payMethod: String? = null
    @JsonProperty("商品说明")
    var explain: String? = null
    @JsonProperty("账单分类")
    var billClass: String? = null
    @JsonProperty("创建时间")
    var createTime: String? = null
    @JsonProperty("订单号")
    var orderNum: String? = null
    @JsonProperty("商户订单号")
    var businessOrderNum: String? = null
    @JsonProperty("积分奖励")
    var rewardPoints: String? = null
    @JsonProperty("对方账户")
    var reciprocalAccount: String? = null
    //转账
    @JsonProperty("转出说明")
    var tansOutInstruction: String? = null
    @JsonProperty("转入账户")
    var transferAccount: String? = null
    //购物
    @JsonProperty("订单金额")
    var billAmount: String? = null
    @JsonProperty("红包")
    var redEnvelope: String? = null
    @JsonProperty("奖励金抵扣")
    var rewardDeduction: String? = null
    //物流
    @JsonProperty("收货地址")
    var shipAddress: String? = null
    @JsonProperty("物流信息")
    var logistics: String? = null
    //充值。"账单分类":"消费-通讯"
    @JsonProperty("充值说明")
    var rechargeInstruction: String? = null
    @JsonProperty("充值号码")
    var rechargeNumber: String? = null
    @JsonProperty("面值")
    var faceValue: String? = null
    @JsonProperty("交易对象")
    var tradingPartners: String? = null
    //预约还款 "账单分类":"借还款"
    @JsonProperty("借还款说明")
    var repaymentInstruction: String? = null
    @JsonProperty("还款到")
    var repaymentTo: String? = null
    //给收款 付款
    @JsonProperty("收款理由")
    var receiptReason: String? = null


    enum class Status(val content: String) {
        Status_Success("交易成功"),
        Status_Closed("交易关闭"),
        Status_Wait_Delivery("等待确认收货"),
        Repayment_Success("还款成功")
    }

    companion object {
        fun parseFromRawJson(raw: BillDetailsRaw): BillDetail {
            if (!raw.rawHtml.isNullOrEmpty()) {
                throw IllegalStateException("RawHtml is not null, parse html to json error : $raw")
            }
            checkNotNull(raw.rawJson, { "Raw json is null" })
            val billDetail = App.app.objectMapper.readValue(raw.rawJson, BillDetail::class.java)
            billDetail.tradeNo = raw.tradeNo
            billDetail.header = raw.header
            billDetail.price = raw.price
            billDetail.status = Status.values().find {
                it.content == raw.status
            }
            checkNotNull(billDetail.status, { "Status:${raw.status}" })
            return billDetail
        }

    }
}