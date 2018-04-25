package com.github.ykrank.alipayxposed.app.data.xls.parser

import com.github.ykrank.alipayxposed.app.business.BillDetail
import com.github.ykrank.alipayxposed.app.data.xls.XlsModel

class YueBaoParser : BillDetailParser {

    override val name = "余额宝"

    override fun parseXlsModel(billDetail: BillDetail): XlsModel? {
        if (billDetail.billClass == "理财" && billDetail.header == "天弘基金管理有限公司") {
            val explain = billDetail.explain
            if (explain != null && explain.startsWith("余额宝") && explain.endsWith("收益发放")) {
                val xlsModel = XlsModel()
                xlsModel.transactionType = XlsModel.TransactionType.Income
                xlsModel.date = billDetail.createTime
                xlsModel.category = "职业收入"
                xlsModel.childCategory = "投资收入"
                xlsModel.account1 = "余额宝"
                xlsModel.amount = billDetail.price
                return xlsModel
            }
        }

        return null
    }
}