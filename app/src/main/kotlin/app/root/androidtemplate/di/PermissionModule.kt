package app.root.androidtemplate.di

import android.app.Application
import android.content.Context
import app.root.androidtemplate.implementation.permission.ActivityProviderImpl
import app.template.base_android.permission.ActivityResultManager
import app.root.androidtemplate.implementation.permission.ActivityResultManagerImpl
import app.root.androidtemplate.implementation.permission.PermissionManagerImpl
import app.template.base.actions.Logger
import app.template.base.actions.permission.PermissionManager
import app.template.base_android.util.ActivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PermissionModule {
    @Provides
    @Singleton
    fun provideActivityProvider(
        @ApplicationContext context: Context,
        logger: Logger
    ): ActivityProvider = ActivityProviderImpl(context as Application, logger)

    @Provides
    @Singleton
    fun provideActivityResultManager(
        activityProvider: ActivityProvider
    ): ActivityResultManager = ActivityResultManagerImpl(activityProvider)

    @Provides
    @Singleton
    fun providePermissionManager(
        activityResultManager: ActivityResultManager,
        activityProvider: ActivityProvider,
        @ApplicationContext context: Context
    ): PermissionManager = PermissionManagerImpl(activityProvider, context, activityResultManager)

}