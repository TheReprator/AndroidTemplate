/*
 * Copyright 2021
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

import app.root.androidtemplate.appinitializers.ActivityProviderInitializer
import app.root.androidtemplate.appinitializers.CoilAppInitializer
import app.root.androidtemplate.appinitializers.PreferencesInitializer
import app.root.androidtemplate.appinitializers.TimberInitializer
import app.root.androidtemplate.implementation.AndroidPowerController
import app.template.base.actions.PowerController
import app.template.base_android.appinitializers.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModuleBinds {

    @Binds
    internal abstract fun providePowerController(bind: AndroidPowerController): PowerController

    @Binds
    @IntoSet
    abstract fun providePreferencesInitializer(bind: PreferencesInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(bind: CoilAppInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideTimberInitializer(bind: TimberInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideActivityProviderInitializer(bind: ActivityProviderInitializer): AppInitializer
}
