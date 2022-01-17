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

package app.root.androidtemplate.di

import android.app.Application
import android.content.Context
import app.root.androidtemplate.implementation.permission.ActivityProviderImpl
import app.root.androidtemplate.implementation.permission.ActivityResultManagerImpl
import app.root.androidtemplate.implementation.permission.PermissionManagerImpl
import app.template.base.actions.Logger
import app.template.base.actions.permission.PermissionManager
import app.template.base_android.permission.ActivityResultManager
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
