package com.github.ykrank.alipayxposed

import android.webkit.JavascriptInterface
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers


const val UC_WEBVIEW_API_CLASS = "com.uc.webview.export.WebView" //libandroid-phone-wallet-nebulaucsdk.so
//const val UC_WEBVIEW_API_CLASS = "com.alipay.mobile.nebulauc.impl.UCWebView" //libandroid-phone-wallet-nebulauc.so
fun hookLoadWebView(classLoader: ClassLoader) {
    //追踪UcWebview加载数据 com.uc.webview.export.WebView
    if (XposedHelpers.findClassIfExists(UC_WEBVIEW_API_CLASS, classLoader) == null) {
        XposedBridge.log("Could not find $UC_WEBVIEW_API_CLASS")
        return
    }
    XposedHelpers.findAndHookMethod(UC_WEBVIEW_API_CLASS, classLoader, "setWebViewClient",
            "com.uc.webview.export.WebViewClient", object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
//            XposedBridge.log("UcWebView:${param.thisObject}")
//            XposedBridge.log("UcWebViewClient:${param.args[0]}")
//            XposedBridge.log(Exception())
            try {
//                val setting = XposedHelpers.callMethod(param.thisObject, "getSettings")
//                if (setting != null) {
//                    val jsEnable = XposedHelpers.callMethod(setting, "getJavaScriptEnabled")
//                    XposedBridge.log("UcSetting:$setting, jsEnable:$jsEnable")
//                    UcWebViewSettingHook.hookLoadWebViewSetting(setting.javaClass.classLoader)
////                    XposedHelpers.callMethod(setting, "setJavaScriptEnabled", true)
//                }
                XposedHelpers.callMethod(param.thisObject, "addJavascriptInterface", MyJavaScriptInterface(), "HTMLOUT")
            } catch (e: Throwable) {
                XposedBridge.log(e)
            }
        }
    })
//    XposedHelpers.findAndHookMethod(UC_WEBVIEW_API_CLASS, classLoader, "setWebChromeClient",
//            "com.uc.webview.export.WebChromeClient", object : XC_MethodHook() {
//        @Throws(Throwable::class)
//        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
//            XposedBridge.log("UcWebView:${param.thisObject}")
//            XposedBridge.log("UcWebChromeClient:${param.args[0]}")
//        }
//    })
    XposedHelpers.findAndHookMethod(UC_WEBVIEW_API_CLASS, classLoader, "addJavascriptInterface",
            Object::class.java, String::class.java, object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
            XposedBridge.log("addJavascriptInterface:${param.thisObject}, ${param.args.toList()}")
//            XposedBridge.log(Exception())
        }
    })
}

fun hookLoadData(classLoader: ClassLoader) {
    //追踪UcWebview加载数据 com.uc.webview.export.WebView
    if (XposedHelpers.findClassIfExists(UC_WEBVIEW_API_CLASS, classLoader) == null) {
        XposedBridge.log("Could not find $UC_WEBVIEW_API_CLASS")
        return
    }
    XposedHelpers.findAndHookMethod(UC_WEBVIEW_API_CLASS, classLoader, "loadData",
            String::class.java, String::class.java, String::class.java, object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
            XposedBridge.log("Uc loadData:${param.args.asList()}")
        }
    })
    XposedHelpers.findAndHookMethod(UC_WEBVIEW_API_CLASS, classLoader, "loadDataWithBaseURL",
            String::class.java, String::class.java, String::class.java, String::class.java, String::class.java, object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
            XposedBridge.log("Uc loadDataWithBaseURL:${param.args.asList()}")
        }
    })
    XposedHelpers.findAndHookMethod(UC_WEBVIEW_API_CLASS, classLoader, "loadUrl",
            String::class.java, object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
            XposedBridge.log("Uc loadUrl:${param.args.asList()}")
        }
    })
    XposedHelpers.findAndHookMethod(UC_WEBVIEW_API_CLASS, classLoader, "loadUrl",
            String::class.java, Map::class.java, object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
            XposedBridge.log("Uc loadUrl:${param.args.asList()}")
        }
    })
}

/* An instance of this class will be registered as a JavaScript interface */
class MyJavaScriptInterface {
    @JavascriptInterface
    fun processHTML(html: String) {
        // process the html as needed by the app
        XposedBridge.log("Html:$html")
    }
}