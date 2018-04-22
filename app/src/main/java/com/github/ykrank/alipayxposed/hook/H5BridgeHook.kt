package com.github.ykrank.alipayxposed.hook

import android.app.Activity
import android.os.Bundle
import com.github.ykrank.alipayxposed.bridge.BillH5ContentValues
import com.github.ykrank.androidtools.util.RxJavaUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import io.reactivex.Single
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


object H5BridgeHook {
    const val H5_PLUGIN_API_CLASS = "com.alipay.mobile.nebulacore.config.H5PluginProxy"
    const val H5_H5PageImpl_CLASS = "com.alipay.mobile.nebulacore.core.H5PageImpl"
    const val H5_EVENT_MY_HOOK = "my_hook"

    fun hookLoadH5Bridge(classLoader: ClassLoader) {
        //Hook 对于H5 AlipayJSBridge 事件的处理。从而截断UcWebViewClientHook在网页中对于数据的处理。
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
                                            //返回上一界面
                                            HookedApp.h5PageImpl?.get()?.let {
                                                Single.just(it)
                                                        .delay(100, TimeUnit.MILLISECONDS)
                                                        .compose(RxJavaUtil.iOSingleTransformer())
                                                        .subscribe({
                                                            XposedHelpers.callMethod(it, "exitPage")
                                                        }, XposedBridge::log)
                                            }
                                        }
                                    }
                                    //Disable alert
                                    param.result = false
                                }
                            }
                        }
                    }
                })

        //Hook H5PageImpl，存下它的实例，从而之后可以手动退出页面。
        XposedHelpers.findAndHookConstructor(H5_H5PageImpl_CLASS, classLoader,
                Activity::class.java, Bundle::class.java, "com.alipay.mobile.nebulacore.ui.H5ViewHolder",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        HookedApp.h5PageImpl = WeakReference(param.thisObject)
                    }
                })
//        XposedHelpers.findAndHookConstructor(H5_H5PageImpl_CLASS, classLoader,
//                Activity::class.java, "com.alipay.mobile.nebulacore.ui.H5ViewHolder",
//                object : XC_MethodHook() {
//                    override fun afterHookedMethod(param: MethodHookParam?) {
//                        super.afterHookedMethod(param)
//                        XposedBridge.log("H5PageImpl Constructor 2")
//                    }
//                })
    }
}