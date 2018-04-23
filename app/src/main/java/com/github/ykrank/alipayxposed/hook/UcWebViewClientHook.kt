package com.github.ykrank.alipayxposed.hook

import com.github.ykrank.alipayxposed.bridge.AppSettingContentValues
import com.github.ykrank.alipayxposed.hook.H5BridgeHook.H5_EVENT_MY_HOOK
import com.github.ykrank.androidtools.util.RxJavaUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import io.reactivex.Single
import java.util.concurrent.TimeUnit

object UcWebViewClientHook {
    //    static final String UC_WEBVIEW_CLIENT_API_CLASS = "com.uc.webview.export.WebViewClient"; //libandroid-phone-wallet-nebulaucsdk.so
    const val UC_WEBVIEW_CLIENT_API_CLASS = "com.alipay.mobile.nebulauc.impl.UCWebViewClient"//libandroid-phone-wallet-nebulauc.so
    const val ARG_URL = "arg_url"

    fun hookLoadWebViewClient(classLoader: ClassLoader) {
        if (XposedHelpers.findClassIfExists(UC_WEBVIEW_CLIENT_API_CLASS, classLoader) == null) {
            XposedBridge.log("Could not find ${UC_WEBVIEW_CLIENT_API_CLASS}")
            return
        }
        XposedHelpers.findAndHookMethod(UC_WEBVIEW_CLIENT_API_CLASS, classLoader, "onPageFinished",
                "com.uc.webview.export.WebView", String::class.java, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                AppSettingContentValues.doIfEnable {
                    //                XposedBridge.log("UcClient onPageFinished:" + param.args.toList())
                    val url = param.args[1] as String?
                    if (url != null && url.contains("http") && url.contains("?tradeNo=")) {
//                    XposedBridge.log("onPageFinished webview:${param.args[0]}")
                        //boolean com.alipay.mobile.nebulacore.wallet.H5LoggerPlugin.interceptEvent(com.alipay.mobile.h5container.api.H5Event, com.alipay.mobile.h5container.api.H5BridgeContext)
                        //目前应用中只能支持AlipayJSBridge
                        Single.just(url)
                                .delay(1000, TimeUnit.MILLISECONDS)
                                .compose(RxJavaUtil.iOSingleTransformer())
                                .subscribe({
                                    XposedHelpers.callMethod(param.args[0], "loadUrl", "javascript:window.AlipayJSBridge.call('toast', {\n" +
                                            "     content: document.getElementsByTagName('body')[0].innerHTML,\n" +
                                            "     type: '$H5_EVENT_MY_HOOK',\n" +
                                            "     url: '$it' \n" +
                                            "}, function(){\n" +
//                                    "     alert(\"Hook成功\");\n" +
                                            "});;")
                                }, XposedBridge::log)
                    }
                }
            }
        })
    }
}