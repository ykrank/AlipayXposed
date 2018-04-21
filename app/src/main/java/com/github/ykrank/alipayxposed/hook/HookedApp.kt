package com.github.ykrank.alipayxposed.hook

import android.app.Application
import java.lang.ref.WeakReference

object HookedApp {
    var app: Application? = null

    /**
     * BillListAdapter 中的数据SingleListItem
     */
    val billSingleListItems = hashSetOf<Any>()
    var billListActivityResume = false
    var billListActivity:WeakReference<Any>?= null
    var billListAdapter:WeakReference<Any>?= null
}