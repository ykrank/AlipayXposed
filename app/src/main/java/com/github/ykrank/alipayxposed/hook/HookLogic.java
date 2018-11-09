package com.github.ykrank.alipayxposed.hook;

import android.app.Application;

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
    private final static String TAG = "Xposed";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (false) {
            return;
        }
        if ("com.eg.android.AlipayGphone".equals(loadPackageParam.packageName)) {
            ClassLoader classLoader = loadPackageParam.classLoader;

            //防止支付宝检测xposed，如果没用到插件，应该移除这里
//            XposedHelpers.findAndHookMethod("java.lang.ClassLoader", classLoader, "loadClass",
//                    String.class, boolean.class, new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            String className = (String) param.args[0];
//                            if (className.contains("de.robv.android.xposed")) {
            //找到检测代码位置用，
//                                Log.w(TAG, "ClassLoader loadClass xposed:" + className);
//                                Log.w(TAG, new Exception());
            //实际代码
//                                if (className.equals("de.robv.android.xposed.XposedHelpers")) {
//                                    Log.w(TAG, new Exception());
//                                }
//                            }
//                        }
//                    });
            if (XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader) != null) {
                //移除检测
                XposedHelpers.findAndHookMethod("com.alipay.mobile.base.security.CI", classLoader, "a",
                        ClassLoader.class, String.class, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log("Alipay security.CI");
                                param.setResult((byte) 0);
                            }
                        });
            }
            if (loadPackageParam.isFirstApplication) {
                //Application
                XposedHelpers.findAndHookMethod(Application.class, "onCreate",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (HookedApp.INSTANCE.getApp() == null) {
                                    HookedApp.INSTANCE.setApp((Application) param.thisObject);
                                    XposedBridge.log("AlipayXposed save application");
                                } else if (HookedApp.INSTANCE.getApp() != param.thisObject) {
                                    XposedBridge.log("AlipayXposed not same application");
                                }
                            }
                        });
                H5BridgeHook.INSTANCE.hookLoadH5Bridge(classLoader);
            }
            //壳中真正的加载类，因为分包的原因，有些类会在之后懒加载
            XposedHelpers.findAndHookMethod("dalvik.system.DexFile", classLoader, "loadClass",
                    String.class, "java.lang.ClassLoader", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            String className = (String) param.args[0];
                            Object result = param.getResult();
                            if (result != null && result instanceof Class) {
                                if (UcWebViewClientHook.UC_WEBVIEW_CLIENT_API_CLASS.equals(className)) {
                                    XposedBridge.log("Load class:" + ((Class) result).getName());
                                    UcWebViewClientHook.INSTANCE.hookLoadWebViewClient(((Class) result).getClassLoader());
                                } else if (BillListHook.Cls_BillListActivity.equals(className)) {
                                    XposedBridge.log("Load class:" + ((Class) result).getName());
                                    BillListHook.INSTANCE.hookBillList(((Class) result).getClassLoader());
                                }
                            }
                        }
                    });
            //这个和上面不会同时发生
            if (XposedHelpers.findClassIfExists(UcWebViewClientHook.UC_WEBVIEW_CLIENT_API_CLASS, classLoader) != null) {
                XposedBridge.log("Hook class:" + UcWebViewClientHook.UC_WEBVIEW_CLIENT_API_CLASS);
                UcWebViewClientHook.INSTANCE.hookLoadWebViewClient(classLoader);
            }
            if (XposedHelpers.findClassIfExists(BillListHook.Cls_BillListActivity, classLoader) != null) {
                XposedBridge.log("Hook class:" + BillListHook.Cls_BillListActivity);
                BillListHook.INSTANCE.hookBillList(classLoader);
            }


//            FragmentHook.INSTANCE.hookCommit(loadPackageParam.classLoader);
//            H5UiHook.hookH5FragmentManager(loadPackageParam.classLoader);

        }
    }
}