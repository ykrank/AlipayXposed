package com.github.ykrank.alipayxposed.hook

import android.app.Application
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

object HookedApp {
    var app: Application? = null

    /**
     * BillListAdapter 中的数据SingleListItem
     */
    val billSingleListItems = ConcurrentHashMap<Any, Boolean>()
    var billListActivityResume = false
    /**
     * @see BillListHook.Cls_BillListActivity
     */
    var billListActivity:WeakReference<Any>?= null
    /**
     * @see BillListHook.Cls_BillListAdapter
     */
    var billListAdapter:WeakReference<Any>?= null

    /**
     * @see H5BridgeHook.H5_H5PageImpl_CLASS
     */
    var h5PageImpl:WeakReference<Any>?= null
}