package com.github.ykrank.alipayxposed.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.Preference
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.BuildConfig
import com.github.ykrank.alipayxposed.R
import com.github.ykrank.alipayxposed.app.business.BillDetail
import com.github.ykrank.alipayxposed.bridge.AppSettingContentValues
import com.github.ykrank.androidtools.extension.toast
import com.github.ykrank.androidtools.ui.LibBasePreferenceFragment
import com.github.ykrank.androidtools.util.L
import com.github.ykrank.androidtools.util.RxJavaUtil
import com.github.ykrank.androidtools.widget.BackupDelegate
import io.reactivex.Observable

/**
 * An Activity includes general settings that allow users
 * to modify general features and behaviors such as theme
 * and font size.
 */
class AppPreferenceFragment : LibBasePreferenceFragment(), Preference.OnPreferenceClickListener {
    private var backupAgent: BackupDelegate? = null

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preference_app)

        findPreference(getString(R.string.pref_key_parse_bill)).onPreferenceClickListener = this
        findPreference(getString(R.string.pref_key_backup_backup)).onPreferenceClickListener = this
        findPreference(getString(R.string.pref_key_backup_restore)).onPreferenceClickListener = this

        backupAgent = BackupDelegate(activity, "AlipayXposed_v${BuildConfig.VERSION_CODE}.bak",
                BuildConfig.DB_NAME, afterRestore = object : BackupDelegate.DefaultAfterRestore(activity) {
            override fun invokeMsg(message: Int) {
                App.app.dbSessionManager.invalidateDaoSession()
                super.invokeMsg(message)
            }
        })
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_key_enable)) {
            activity?.contentResolver?.notifyChange(AppSettingContentValues.getTableUri(), null)
        }
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            getString(R.string.pref_key_parse_bill) -> {
                App.app.billDb.getAllList()
                        .compose(RxJavaUtil.iOTransformer())
                        .flatMap { Observable.fromIterable(it) }
                        .subscribe({
                            BillDetail.parseFromRawJson(it)
                        }, L::e, { activity.toast("Parse success") })
                return true
            }
            getString(R.string.pref_key_backup_backup) -> {
                backupAgent?.backup(this)
                return true
            }
            getString(R.string.pref_key_backup_restore) -> {
                App.app.dbSessionManager.closeDb()
                backupAgent?.restore(this)
                return true
            }
            else -> return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (backupAgent?.onActivityResult(requestCode, resultCode, data) != true)
            super.onActivityResult(requestCode, resultCode, data)
    }
}
