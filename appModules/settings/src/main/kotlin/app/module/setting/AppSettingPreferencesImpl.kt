package app.module.setting

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import app.template.base.actions.AppSettingPreferences
import app.template.base.actions.AppSettingPreferences.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppSettingPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences
) : AppSettingPreferences {
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            KEY_THEME -> updateUsingThemePreference()
        }
    }

    private val defaultThemeValue = context.getString(R.string.pref_theme_default_value)

    companion object {
        const val KEY_THEME = "pref_theme"
        const val KEY_DATA_SAVER = "pref_data_saver"
    }

    override fun setup() {
        updateUsingThemePreference()
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override var theme: Theme
        get() = getThemeForStorageValue(sharedPreferences.getString(KEY_THEME, defaultThemeValue)!!)
        set(value) = sharedPreferences.edit {
            putString(KEY_THEME, value.storageKey)
        }

    override var useLessData: Boolean
        get() = sharedPreferences.getBoolean(KEY_DATA_SAVER, false)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_DATA_SAVER, value)
        }

    val Theme.storageKey: String
        get() = when (this) {
            Theme.LIGHT -> context.getString(R.string.pref_theme_light_value)
            Theme.DARK -> context.getString(R.string.pref_theme_dark_value)
            Theme.SYSTEM -> context.getString(R.string.pref_theme_system_value)
        }

    private fun getThemeForStorageValue(value: String) = when (value) {
        context.getString(R.string.pref_theme_light_value) -> Theme.LIGHT
        context.getString(R.string.pref_theme_dark_value) -> Theme.DARK
        else -> Theme.SYSTEM
    }

    private fun updateUsingThemePreference() = when (theme) {
        Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Theme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
