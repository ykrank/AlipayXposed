package com.github.ykrank.alipayxposed

import android.app.Application
import android.support.multidex.MultiDexApplication

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        app = this
    }

    companion object {
        lateinit var app: Application
    }
}