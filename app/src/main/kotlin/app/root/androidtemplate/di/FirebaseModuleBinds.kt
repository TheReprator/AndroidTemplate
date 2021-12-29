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

import app.root.androidtemplate.implementation.AppAnalytics
import app.root.androidtemplate.implementation.AppLogger
import app.template.base.util.Analytics
import app.template.base.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class FirebaseModule {
    @Singleton
    @Binds
    internal abstract fun provideLogger(bind: AppLogger): Logger

    @Singleton
    @Binds
    internal abstract fun provideAnalytics(bind: AppAnalytics): Analytics
}
