package com.github.ykrank.alipayxposed.hook

import android.os.Bundle
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.util.*

object FragmentHook {
    const val CLASS_BackStackRecord = "android.support.v4.app.BackStackRecord"
    const val CLASS_Fragment = "android.support.v4.app.Fragment"

    fun hookCommit(classLoader: ClassLoader) {
        XposedHelpers.findAndHookMethod(CLASS_BackStackRecord, classLoader, "commit",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                        XposedBridge.log("Fragment commit:" + Arrays.asList(param.args))
                        XposedBridge.log(Exception())
                    }
                })
        XposedHelpers.findAndHookMethod(CLASS_BackStackRecord, classLoader, "commitAllowingStateLoss",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                        XposedBridge.log("Fragment commitAllowingStateLoss:" + Arrays.asList(param.args))
                        XposedBridge.log(Exception())
                    }
                })

        try {
            //低版本的Fragment没有这个方法
            XposedHelpers.findAndHookMethod(CLASS_BackStackRecord, classLoader, "commitNow",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                            XposedBridge.log("Fragment commitNow:" + Arrays.asList(param.args))
                            XposedBridge.log(Exception())
                        }
                    })
            XposedHelpers.findAndHookMethod(CLASS_BackStackRecord, classLoader, "commitNowAllowingStateLoss",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                            XposedBridge.log("Fragment commitNowAllowingStateLoss:" + Arrays.asList(param.args))
                            XposedBridge.log(Exception())
                        }
                    })
        } catch (e: Exception) {

        }
    }
}