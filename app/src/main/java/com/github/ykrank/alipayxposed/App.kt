package com.github.ykrank.alipayxposed

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.github.ykrank.androidtools.DefaultAppDataProvider
import com.github.ykrank.androidtools.GlobalData
import com.github.ykrank.androidtools.util.L

class App : MultiDexApplication() {

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
    }

    companion object {
        lateinit var app: Application
    }
}