package com.github.ykrank.alipayxposed.app.data.xls

import android.content.Context
import com.github.ykrank.androidtools.util.FileUtil
import com.github.ykrank.androidtools.util.L
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem
import java.io.File
import java.util.*


object Poi {
    const val Assets_Xls = "template.xls"

    fun readXls(context: Context, fileName:String) {
        try {
            val fs = NPOIFSFileSystem(context.assets.open(Assets_Xls))
            val wb = HSSFWorkbook(fs.root, true)
            val sheet = wb.getSheet("支出")
            var index = sheet.firstRowNum
            val num = sheet.physicalNumberOfRows
            val firstRow = sheet.getRow(index)
            XlsModel.initCellIndex(firstRow)

            val secondRow = sheet.createRow(index+num)

            L.d("index:$index, num:$num")

            val xlsModel = XlsModel()
            xlsModel.transactionType = XlsModel.TransactionType.Expenses
            xlsModel.amount = 233.3
            xlsModel.date = Date()
            xlsModel.remark = "Remark"
            xlsModel.writeToRow(secondRow)

            val dir = FileUtil.getDownloadDirectory(context)
            val file = File(dir, fileName)
            wb.write(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}