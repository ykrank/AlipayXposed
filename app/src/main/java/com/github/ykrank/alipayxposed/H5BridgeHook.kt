package com.github.ykrank.alipayxposed

import com.github.ykrank.androidtools.util.FileUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.io.File

const val H5_PLUGIN_API_CLASS = "com.alipay.mobile.nebulacore.config.H5PluginProxy"
const val H5_EVENT_MY_HOOK = "my_hook"

fun hookLoadH5Bridge(classLoader: ClassLoader) {
    if (XposedHelpers.findClassIfExists(H5_PLUGIN_API_CLASS, classLoader) == null) {
        XposedBridge.log("Could not find $H5_PLUGIN_API_CLASS")
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
                                        //FIXME 此处是在hook的目标app中运行，需要传出至自己的APP中处理
                                        val file = File(FileUtil.getDownloadDirectory(App.app), tradeNo)
                                        XposedBridge.log("Write trade html:${file.absolutePath}")
                                        file.writeText(content!!)
                                        XposedBridge.log("interceptEvent h5event: action: $action ,content:${content}")
                                    }
                                }
                            }
                        }
                    }
                }
            })
}
