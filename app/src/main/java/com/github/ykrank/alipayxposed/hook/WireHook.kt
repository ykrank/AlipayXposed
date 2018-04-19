package com.github.ykrank.alipayxposed.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

fun hookParseData(classLoader: ClassLoader) {
    //显示protocol RPC接收到的数据
    XposedHelpers.findAndHookMethod("com.squareup.wire.Wire", classLoader, "parseFrom",
            "com.squareup.wire.WireInput", "java.lang.Class", object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
            val result = param!!.result
            if (result != null) {
                //                                XposedBridge.log("Wire output:" + result.getClass().getName());
                //                                XposedBridge.log(result.toString());
                //账单列表
                if ("com.alipay.mobilebill.common.service.model.pb.QueryListRes" == result.javaClass.name) {

                }
            }
        }
    })
}