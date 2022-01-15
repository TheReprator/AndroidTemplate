@file:Suppress("DEPRECATION")

package app.module.setting

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import app.template.base.actions.AppSettingPreferences
import app.template.navigation.AppNavigator
import app.template.navigation.SettingNavigator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class SettingModuleBinds {
    @Singleton
    @Binds
    abstract fun providePreferences(bind: AppSettingPreferencesImpl): AppSettingPreferences
}

@InstallIn(SingletonComponent::class)
@Module
class SettingModule {
    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}


@InstallIn(FragmentComponent::class)
@Module
class SettingFragmentModule {

    @Provides
    fun provideSettingNavigator( appNavigator: AppNavigator): SettingNavigator {
        return appNavigator
    }
}