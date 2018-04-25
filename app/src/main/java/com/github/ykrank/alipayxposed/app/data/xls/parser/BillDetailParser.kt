package com.github.ykrank.alipayxposed.app.data.xls.parser

import com.github.ykrank.alipayxposed.app.business.BillDetail
import com.github.ykrank.alipayxposed.app.data.xls.XlsModel

interface BillDetailParser {

    /**
     * 解析器名称，保存在数据库中
     */
    val name:String

    /**
     * 解析出对应的XlsModel。如果不是对应的则返回Null，如果忽略则是 XlsModel.EMPTY
     */
    fun parseXlsModel(billDetail: BillDetail): XlsModel?
}