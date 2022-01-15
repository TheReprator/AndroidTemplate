@file:Suppress("DEPRECATION")

package app.module.modulea

import app.template.navigation.AppNavigator
import app.template.navigation.FeatureNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(FragmentComponent::class)
@Module
class ModuleFeatureModule {

    @Provides
    fun provideFeatureNavigator(
        appNavigator: AppNavigator
    ): FeatureNavigator {
        return appNavigator
    }
}