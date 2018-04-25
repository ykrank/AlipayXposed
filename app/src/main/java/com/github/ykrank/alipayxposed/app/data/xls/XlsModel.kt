package com.github.ykrank.alipayxposed.app.data.xls

import org.apache.poi.ss.usermodel.Row
import java.text.SimpleDateFormat
import java.util.*

class XlsModel {
    /**
     * 交易类型
     */
    var transactionType: TransactionType? = null
    /**
     * 日期 2017-07-21 18:36:54
     */
    var date: Date? = null
    /**
     * 分类
     */
    var category:String? = null
    /**
     * 子分类
     */
    var childCategory:String? = null
    /**
     * 账户1
     */
    var account1:String? = null
    /**
     * 账户2
     */
    var account2:String? = null
    /**
     * 金额
     */
    var amount:Double = 0.0
    /**
     * 成员
     */
    var member:String? = "本人"
    /**
     * 商家
     */
    var business:String? = null
    /**
     * 项目
     */
    var project:String? = null
    /**
     * 备注
     */
    var remark:String? = null


    fun writeToRow(row:Row): Row {
        row.createCell(CellIndex[0]).setCellValue(transactionType?.cName)
        row.createCell(CellIndex[1]).setCellValue(sdf.format(date))
        row.createCell(CellIndex[2]).setCellValue(category)
        row.createCell(CellIndex[3]).setCellValue(childCategory)
        row.createCell(CellIndex[4]).setCellValue(account1)
        row.createCell(CellIndex[5]).setCellValue(account2)
        row.createCell(CellIndex[6]).setCellValue(amount)
        row.createCell(CellIndex[7]).setCellValue(member)
        row.createCell(CellIndex[8]).setCellValue(business)
        row.createCell(CellIndex[9]).setCellValue(project)
        row.createCell(CellIndex[10]).setCellValue(remark)

        return row
    }

    enum class TransactionType(val cName: String) {
        Expenses("支出"),
        Income("收入"),
        Transfer("转账")
    }

    companion object {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        /**
         * 字段在Excel中的列编号（从0开始）
         */
        var CellIndex = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        /**
         * 使用标题行初始化列编号
         */
        fun initCellIndex(firstRow:Row){
            firstRow.cellIterator().forEach {
                when(it.stringCellValue){
                    "交易类型" -> CellIndex[0] = it.columnIndex
                    "日期" -> CellIndex[1] = it.columnIndex
                    "分类" -> CellIndex[2] = it.columnIndex
                    "子分类" -> CellIndex[3] = it.columnIndex
                    "账户1" -> CellIndex[4] = it.columnIndex
                    "账户2" -> CellIndex[5] = it.columnIndex
                    "金额" -> CellIndex[6] = it.columnIndex
                    "成员" -> CellIndex[7] = it.columnIndex
                    "商家" -> CellIndex[8] = it.columnIndex
                    "项目" -> CellIndex[9] = it.columnIndex
                    "备注" -> CellIndex[10] = it.columnIndex
                }
            }
        }
    }
}