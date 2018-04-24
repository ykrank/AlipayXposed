package com.github.ykrank.alipayxposed.app.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.github.ykrank.alipayxposed.R
import com.github.ykrank.androidtools.data.BasePreferences
import com.github.ykrank.androidtools.data.PreferenceDelegates

class AppPreferencesImpl(context: Context, sharedPreferences: SharedPreferences)
    : BasePreferences(context, sharedPreferences), AppPreferences {

    override var enable: Boolean by PreferenceDelegates.bool(
            R.string.pref_key_enable, R.bool.pref_enable_default_value)

    override var debugBillDetail: Boolean by PreferenceDelegates.bool(
            R.string.pref_key_debug_bill_detail, R.bool.pref_debug_bill_details_default_value)
}

interface AppPreferences {
    var enable: Boolean
    var debugBillDetail: Boolean
}

class AppPreferencesManager(private val mPreferencesProvider: AppPreferences) : AppPreferences by mPreferencesProvider