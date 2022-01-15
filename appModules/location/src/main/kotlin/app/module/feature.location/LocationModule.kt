@file:Suppress("DEPRECATION")

package app.module.feature.location

import app.template.navigation.AppNavigator
import app.template.navigation.LocationNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(FragmentComponent::class)
@Module
class LocationModule {

    @Provides
    fun provideLocationNavigator( appNavigator: AppNavigator ): LocationNavigator {
        return appNavigator
    }
}