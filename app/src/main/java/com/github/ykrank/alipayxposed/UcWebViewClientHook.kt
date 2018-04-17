package com.github.ykrank.alipayxposed

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

//    static final String UC_WEBVIEW_CLIENT_API_CLASS = "com.uc.webview.export.WebViewClient"; //libandroid-phone-wallet-nebulaucsdk.so
const val UC_WEBVIEW_CLIENT_API_CLASS = "com.alipay.mobile.nebulauc.impl.UCWebViewClient"//libandroid-phone-wallet-nebulauc.so
const val ARG_URL = "arg_url"

fun hookLoadWebViewClient(classLoader: ClassLoader) {
    if (XposedHelpers.findClassIfExists(UC_WEBVIEW_CLIENT_API_CLASS, classLoader) == null) {
        XposedBridge.log("Could not find $UC_WEBVIEW_CLIENT_API_CLASS")
        return
    }
//    XposedHelpers.findAndHookMethod(UC_WEBVIEW_CLIENT_API_CLASS, classLoader, "onPageFinished",
//            "com.uc.webview.export.WebView", String::class.java, object : XC_MethodHook() {
//        @Throws(Throwable::class)
//        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
//            XposedBridge.log("UcClient onPageFinished:" + param.args.toList())
//            val url = param.args[1] as String?
//            if (url != null && url.contains("http")) {
//
//                XposedHelpers.callMethod(param.args[0], "loadUrl",
//                        "javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
//            }
//        }
//    })
    XposedHelpers.findAndHookMethod(UC_WEBVIEW_CLIENT_API_CLASS, classLoader, "shouldInterceptRequest",
            "com.uc.webview.export.WebView", String::class.java, object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
            XposedBridge.log("UcClient shouldInterceptRequest:" + param.args.toList())
            val url = param.args[1] as String?
            //账单详情的网络请求
            if (url != null && url.contains("http") && url.contains("?tradeNo=")) {
                //com.uc.webview.export.WebResourceResponse
                val result = param.result
                if (result != null) {
                    hookInputStreamGetData(result.javaClass)
                    XposedBridge.log("shouldInterceptRequest result:" + param.result)
                    //将url通过额外字段传递到inputstream中
                    XposedHelpers.setAdditionalInstanceField(result, ARG_URL, url)
                }
            }
        }
    })
}

fun hookInputStreamGetData(cls: Class<Any>) {
    XposedHelpers.findAndHookMethod(cls, "getData", object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) {
            //java.io.ByteArrayInputStream
//                            XposedBridge.log("getData result:" + param.result)
            //将url通过额外字段传递到inputstream中
            val url = XposedHelpers.getAdditionalInstanceField(param.thisObject, ARG_URL)
            XposedHelpers.setAdditionalInstanceField(param.result, ARG_URL, url)
        }
    })
}