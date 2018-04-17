package com.github.ykrank.alipayxposed;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class UcWebViewSettingHook {
//    static final String UC_WEBVIEW_SETTING_API_CLASS = "com.uc.webview.export.WebSettings"; //libandroid-phone-wallet-nebulaucsdk.so
    static final String UC_WEBVIEW_SETTING_API_CLASS = "com.uc.sdk_glue.bg"; //继承抽象类com.uc.webview.export.WebSettings

    public static void hookLoadWebViewSetting(ClassLoader classLoader) {
        if (XposedHelpers.findClassIfExists(UC_WEBVIEW_SETTING_API_CLASS, classLoader) == null) {
            XposedBridge.log("Could not find " + UC_WEBVIEW_SETTING_API_CLASS);
            return;
        }
        XposedHelpers.findAndHookMethod(UC_WEBVIEW_SETTING_API_CLASS, classLoader, "setJavaScriptEnabled",
                boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("UcSetting setJavaScriptEnabled:" + Arrays.asList(param.args));
                    }
                });
    }
}
