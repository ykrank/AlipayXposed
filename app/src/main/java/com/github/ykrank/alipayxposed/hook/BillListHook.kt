package com.github.ykrank.alipayxposed.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.ref.WeakReference

object BillListHook {

    const val Cls_BillListAdapter = "com.alipay.mobile.bill.list.ui.adapter.BillListAdapter"
    const val Cls_BillListActivity = "com.alipay.mobile.bill.list.ui.BillListActivity" //实际上是BillListActivity_

    fun hookBillList(classLoader: ClassLoader) {
        if (XposedHelpers.findClassIfExists(Cls_BillListActivity, classLoader) == null) {
            XposedBridge.log("Could not find $Cls_BillListActivity")
            return
        }
        //adapter addAll
        XposedHelpers.findAndHookMethod(Cls_BillListAdapter, classLoader, "a",
                List::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
//                XposedBridge.log(Exception("BillListAdapter"))
//                XposedBridge.log("AddAll:${param.args[0]}")
                HookedApp.billListAdapter = WeakReference(param.thisObject)
                HookedApp.billSingleListItems.addAll(param.args[0] as Collection<Any>)
                goNextH5()
            }
        })
        //onResume
        XposedHelpers.findAndHookMethod(Cls_BillListActivity, classLoader, "onResume",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
//                        XposedBridge.log("BillListActivity onResume")
                            HookedApp.billListActivityResume = true
                            HookedApp.billListActivity = WeakReference(param.thisObject)
                            //Get adapter
                            val adapter = XposedHelpers.getObjectField(param.thisObject, "z")
                            val clsName = adapter?.javaClass?.name
                            if (clsName != Cls_BillListAdapter) {
                                XposedBridge.log("Could not find BillListAdapter:$clsName")
                                return
                            }
                            //Get adapter data
                            val data = XposedHelpers.getObjectField(adapter, "a")
//                        XposedBridge.log("Adapter data:$data")
                            HookedApp.billSingleListItems.addAll(data as Collection<Any>)
                            goNextH5()
                        } catch (e: Throwable) {
                            XposedBridge.log(e)
                        }
                    }
                })
        XposedHelpers.findAndHookMethod(Cls_BillListActivity, classLoader, "onPause",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        HookedApp.billListActivityResume = false
                    }
                })
    }

    fun goNextH5() {
        if (HookedApp.billListActivityResume) {
            XposedBridge.log("billSingleListItems: ${HookedApp.billSingleListItems}")
            HookedApp.billSingleListItems.find {
                isItemUndo(it)
            }?.let { goBillH5Page(it) }
        } else {
            XposedBridge.log("BillListActivity paused")
        }
    }

    /**
     * item是否已被处理过。即是否已解析保存在数据库中。
     * @param item SingleListItem
     */
    fun isItemUndo(item: Any): Boolean {
        //TODO 访问ContentProvider来判断
        return true
    }

    /**
     * 跳转到订单详情界面。
     * @param item SingleListItem
     */
    fun goBillH5Page(item: Any) {
        val activity = HookedApp.billListActivity?.get()
        if (activity != null) {
            //c(SingleListItem)
            try {
                XposedHelpers.callMethod(activity, "c", item)
            } catch (e: Throwable) {
                XposedBridge.log(e)
            }
        }
    }
}