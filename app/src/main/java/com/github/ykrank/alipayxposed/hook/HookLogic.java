package com.github.ykrank.alipayxposed.hook;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author DX
 * 注意：该类不要自己写构造方法，否者可能会hook不成功
 * 开发Xposed模块完成以后，建议修改xposed_init文件，并将起指向这个类,以提升性能
 * 所以这个类需要implements IXposedHookLoadPackage,以防修改xposed_init文件后忘记
 * Created by DX on 2017/10/4.
 */

public class HookLogic implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (false) {
            return;
        }
        if ("com.eg.android.AlipayGphone".equals(loadPackageParam.packageName)) {
            //防止支付宝检测xposed，如果没用到插件，应该移除这里
            XposedHelpers.findAndHookMethod("java.lang.ClassLoader", loadPackageParam.classLoader, "loadClass",
                    String.class, boolean.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String className = (String) param.args[0];
                            if (className.contains("xposed")) {
                                XposedBridge.log("ClassLoader loadClass xposed:" + className);
                                param.setResult(null);
                            }
                        }
                    });
            //Application
            if (loadPackageParam.isFirstApplication){
                XposedHelpers.findAndHookMethod(Application.class, "onCreate",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (HookedApp.INSTANCE.getApp() == null){
                                    HookedApp.INSTANCE.setApp((Application) param.thisObject);
                                    XposedBridge.log("AlipayXposed save application");
                                } else if (HookedApp.INSTANCE.getApp() != param.thisObject){
                                    XposedBridge.log("AlipayXposed not same application");
                                }
                            }
                        });
            }
            //壳中真正的加载类
            XposedHelpers.findAndHookMethod("dalvik.system.DexFile", loadPackageParam.classLoader, "loadClass",
                    String.class, "java.lang.ClassLoader", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            String className = (String) param.args[0];
                            Object result = param.getResult();
                            if (result != null && result instanceof Class) {
                                if (UcWebViewClientHookKt.UC_WEBVIEW_CLIENT_API_CLASS.equals(className)) {
                                    XposedBridge.log("Load class:" + ((Class) result).getName());
                                    UcWebViewClientHookKt.hookLoadWebViewClient(((Class) result).getClassLoader());
                                }
                            }
                        }
                    });
            //Hook nebula
            H5BridgeHookKt.hookLoadH5Bridge(loadPackageParam.classLoader);

//            FragmentHook.INSTANCE.hookCommit(loadPackageParam.classLoader);
            H5UiHook.hookH5FragmentManager(loadPackageParam.classLoader);
        }
    }
}