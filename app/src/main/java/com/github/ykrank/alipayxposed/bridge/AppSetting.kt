package com.github.ykrank.alipayxposed.bridge

import android.database.Cursor
import android.database.MatrixCursor
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferences
import com.github.ykrank.alipayxposed.hook.HookedApp

class AppSetting {
    var enable: Boolean = false
    var debugBillDetail = false

    fun toCursor(): Cursor {
        return MatrixCursor(arrayOf(Key_Enable, Key_DebugBillDetails)).apply {
            addRow(arrayOf(if (enable) 1 else 0, if (debugBillDetail) 1 else 0))
        }
    }

    companion object {
        val Key_Enable = "enable" //boolean
        val Key_DebugBillDetails = "debugBillDetails" //boolean

        /**
         * 只有App进程才能调用
         */
        fun fromPref(appPref: AppPreferences): AppSetting {
            val setting = AppSetting()
            setting.enable = appPref.enable
            setting.debugBillDetail =appPref.debugBillDetail
            return setting
        }

        /**
         * 可远程调用
         */
        fun remoteQuery(): AppSetting? {
            val cursor = HookedApp.app?.contentResolver?.query(AppSettingContentValues.getTableUri(),
                    null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val setting = AppSetting()
                    setting.enable = cursor.getInt(0) == 1
                    setting.debugBillDetail = cursor.getInt(1) == 1

                    cursor.close()
                    return setting
                }
                cursor.close()
            }

            return null
        }
    }
}