package com.github.ykrank.alipayxposed

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

const val UC_H5_EVENT_API_CLASS = "com.alipay.mobile.nebulacore.config.H5PluginProxy"

fun hookLoadH5Bridge(classLoader: ClassLoader) {
    if (XposedHelpers.findClassIfExists(UC_H5_EVENT_API_CLASS, classLoader) == null) {
        XposedBridge.log("Could not find $UC_H5_EVENT_API_CLASS")
        return
    }
    XposedHelpers.findAndHookMethod(UC_H5_EVENT_API_CLASS, classLoader, "interceptEvent",
            "com.alipay.mobile.h5container.api.H5Event", "com.alipay.mobile.h5container.api.H5BridgeContext",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    XposedBridge.log("interceptEvent: ${param.args.toList()}")
                }
            })
//    val cls = XposedHelpers.findClass(UC_H5_EVENT_API_CLASS, classLoader)
//    val methods = cls.declaredMethods
//    methods.forEach {
//        XposedBridge.log("    $it")
//    }
}
