package com.github.ykrank.alipayxposed.hook

import android.content.Intent
import com.github.ykrank.alipayxposed.bridge.BillH5ContentValues
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

const val H5_PLUGIN_API_CLASS = "com.alipay.mobile.nebulacore.config.H5PluginProxy"
const val H5_EVENT_MY_HOOK = "my_hook"

fun hookLoadH5Bridge(classLoader: ClassLoader) {
    if (XposedHelpers.findClassIfExists(H5_PLUGIN_API_CLASS, classLoader) == null) {
        XposedBridge.log("Could not find ${H5_PLUGIN_API_CLASS}")
        return
    }
    XposedHelpers.findAndHookMethod(H5_PLUGIN_API_CLASS, classLoader, "interceptEvent",
            "com.alipay.mobile.h5container.api.H5Event", "com.alipay.mobile.h5container.api.H5BridgeContext",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val h5event = param.args[0]
                    val action = XposedHelpers.getObjectField(h5event, "action") as String?
                    if ("toast" == action) {
                        //com.alibaba.fastjson.JSONObject
                        val eventParam = XposedHelpers.getObjectField(h5event, "param")
                        if (eventParam != null) {
                            val type = XposedHelpers.callMethod(eventParam, "getString", "type") as String?
                            if (type == H5_EVENT_MY_HOOK) {
                                val content = XposedHelpers.callMethod(eventParam, "getString", "content") as String?
                                if (!content.isNullOrBlank()) {
                                    val url = XposedHelpers.callMethod(eventParam, "getString", "url") as String?
                                    XposedBridge.log("url:$url")
                                    val tradeNo = url?.substringAfter("tradeNo=")?.substringBefore("&")
                                    XposedBridge.log("tradeNo:$tradeNo")
                                    if (!tradeNo.isNullOrBlank()) {
                                        XposedBridge.log(HookedApp.app.toString())
                                        val uri = BillH5ContentValues.getTableUri()
                                        HookedApp.app?.contentResolver?.insert(uri, BillH5ContentValues.createContentValues(tradeNo, content))
                                    }
                                }
                            }
                        }
                    }
                }
            })
}
