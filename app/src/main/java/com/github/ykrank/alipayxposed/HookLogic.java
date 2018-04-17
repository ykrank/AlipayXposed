package com.github.ykrank.alipayxposed;

import java.io.ByteArrayInputStream;

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
        if (true){
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
            //网络加载的数据，目标数据InputStream已被添加额外字段
            XposedHelpers.findAndHookMethod(ByteArrayInputStream.class, "read",
                    byte[].class, int.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object url = XposedHelpers.getAdditionalInstanceField(param.thisObject, UcWebViewClientHookKt.ARG_URL);
                            if (url != null) {
                                XposedBridge.log(new Exception());
                            }
                        }
                    });
        }
    }
}