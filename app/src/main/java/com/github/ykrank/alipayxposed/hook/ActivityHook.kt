package com.github.ykrank.alipayxposed.hook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.util.*

object ActivityHook {

    fun hookStart() {
        XposedHelpers.findAndHookMethod(Activity::class.java, "startActivity",
                Intent::class.java, Bundle::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                XposedBridge.log("startActivity:" + param.thisObject)
                XposedBridge.log("args:" + Arrays.asList(param.args))
                XposedBridge.log(Exception())
            }
        })
    }
}