package com.github.ykrank.alipayxposed

import android.support.multidex.MultiDexApplication
import android.support.v7.preference.PreferenceManager
import com.facebook.stetho.Stetho
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ykrank.alipayxposed.app.data.db.AppDaoOpenHelper
import com.github.ykrank.alipayxposed.app.data.db.AppDaoSessionManager
import com.github.ykrank.alipayxposed.app.data.db.BillDetailsRawDbWrapper
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferences
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferencesImpl
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferencesManager
import com.github.ykrank.androidtools.DefaultAppDataProvider
import com.github.ykrank.androidtools.GlobalData
import com.github.ykrank.androidtools.util.L

class App : MultiDexApplication() {

    val objectMapper = ObjectMapper()

    lateinit var appPref: AppPreferences
    lateinit var billDb: BillDetailsRawDbWrapper

    override fun onCreate() {
        super.onCreate()

        app = this

        GlobalData.init(object : DefaultAppDataProvider() {
            override val logTag: String
                get() = "AlipayXposed"
            override val appR: Class<out Any>
                get() = R::class.java
        })

        L.init(this)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        appPref = AppPreferencesManager(AppPreferencesImpl(this, pref))

        val dbHelper = AppDaoOpenHelper(this, BuildConfig.DB_NAME)
        val dbManager = AppDaoSessionManager(dbHelper)
        billDb = BillDetailsRawDbWrapper(dbManager)

        Stetho.initializeWithDefaults(this)
    }

    companion object {
        lateinit var app: App
    }
}