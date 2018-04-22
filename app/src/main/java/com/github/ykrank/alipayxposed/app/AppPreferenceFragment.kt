package com.github.ykrank.alipayxposed.app

import android.content.SharedPreferences
import android.os.Bundle

import com.github.ykrank.alipayxposed.R
import com.github.ykrank.alipayxposed.bridge.AppSettingContentValues
import com.github.ykrank.androidtools.ui.LibBasePreferenceFragment

/**
 * An Activity includes general settings that allow users
 * to modify general features and behaviors such as theme
 * and font size.
 */
class AppPreferenceFragment : LibBasePreferenceFragment() {

    override fun onCreatePreferences(bundle: Bundle, s: String) {
        addPreferencesFromResource(R.xml.preference_app)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_key_enable)) {
            activity?.contentResolver?.notifyChange(AppSettingContentValues.getTableUri(), null)
        }
    }

}
