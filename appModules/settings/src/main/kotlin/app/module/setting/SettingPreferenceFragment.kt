/*
 * Copyright 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("DEPRECATION")

package app.module.setting

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import app.template.base.actions.PowerController
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

    @Inject
    lateinit var powerController: PowerController

    private var saveData: SaveData? = null
        set(value) {
            val pref: SwitchPreference = findPreference("pref_data_saver")!!

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            powerController.observeShouldSaveData(ignorePreference = true).collect { saveData ->
                this@SettingPreferenceFragment.saveData = saveData
            }
        }
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
