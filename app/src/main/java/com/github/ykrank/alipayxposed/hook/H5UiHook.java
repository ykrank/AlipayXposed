package com.github.ykrank.alipayxposed.hook;

import android.os.Bundle;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class H5UiHook {
    static final String Class_H5FragmentManager = "com.alipay.mobile.nebulacore.ui.H5FragmentManager";
    static final String Class_H5Fragment = "com.alipay.mobile.nebulacore.ui.H5Fragment";

    public static void hookH5FragmentManager(ClassLoader classLoader){
        XposedHelpers.findAndHookMethod(Class_H5FragmentManager, classLoader, "addFragment",
                Bundle.class, boolean.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("addFragment:"+ Arrays.asList(param.args));
                    }
                });
        XposedHelpers.findAndHookMethod(Class_H5FragmentManager, classLoader, "detachFragment",
                FragmentHook.CLASS_Fragment, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("detachFragment:"+ Arrays.asList(param.args));
                    }
                });
        XposedHelpers.findAndHookMethod(Class_H5FragmentManager, classLoader, "peekFragment",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("peekFragment:"+ param.getResult());
                    }
                });
        XposedHelpers.findAndHookMethod(Class_H5FragmentManager, classLoader, "removeFragment",
                Class_H5Fragment, Bundle.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("removeFragment:"+ Arrays.asList(param.args));

                    }
                });
    }
}
