/*
 * Copyright 2021 Vikram LLC
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

import android.content.Context
import app.template.base.util.permission.PermissionHelper
import app.template.navigation.AppNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import app.root.androidtemplate.implementation.AppNavigatorImpl
import app.root.androidtemplate.implementation.PermissionHelperImpl

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {

    @ActivityScoped
    @Provides
    fun providePermissionHelper(@ActivityContext context: Context): PermissionHelper {
        return PermissionHelperImpl(context)
    }

    @ActivityScoped
    @Provides
    fun provideWillyNavigator(): AppNavigator {
        return AppNavigatorImpl()
    }
}
