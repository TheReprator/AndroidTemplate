@file:Suppress("DEPRECATION")

package app.module.setting

import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.SwitchPreference
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.net.toUri
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.template.base.actions.SaveData
import app.template.base.actions.SaveDataReason
import app.template.base_android.extensions.resolveThemeColor
import app.template.navigation.SettingNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingPreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var settingNavigator: SettingNavigator

    internal var saveData: SaveData? = null
        set(value) {
            val pref = findPreference<Preference?>("pref_data_saver") as? SwitchPreference
                ?: throw IllegalStateException()

            pref.isEnabled = when (value) {
                is SaveData.Enabled -> value.reason == SaveDataReason.PREFERENCE
                else -> true
            }

            if (pref.isEnabled) {
                pref.summary = null
                pref.setSummaryOn(R.string.settings_data_saver_summary_on)
            } else {
                pref.summaryOn = null
                pref.setSummary(R.string.settings_data_saver_summary_system)
            }

            field = value
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference?>("privacy_policy")?.setOnPreferenceClickListener {
            CustomTabsIntent.Builder()
                .setToolbarColor(context!!.resolveThemeColor(android.R.attr.colorPrimary))
                .build()
                .launchUrl(context!!, getString(R.string.privacy_policy_url).toUri())
            true
        }

        findPreference<Preference?>("version")?.apply {
            val pkgManager: PackageManager = context.packageManager
            val pkgInfo = pkgManager.getPackageInfo(context.packageName, 0)
            summary = getString(
                R.string.settings_app_version_summary,
                pkgInfo.versionName,
                PackageInfoCompat.getLongVersionCode(pkgInfo)
            )
        }
    }
}
