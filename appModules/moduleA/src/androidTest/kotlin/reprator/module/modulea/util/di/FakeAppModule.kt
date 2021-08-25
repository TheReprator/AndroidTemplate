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

package reprator.module.modulea.util.di

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.testing.TestLifecycleOwner
import app.template.base.util.interent.ConnectionDetector
import app.template.base.util.network.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

@InstallIn(SingletonComponent::class)
@Module
class FakeAppModule {

    @Provides
    fun provideLifeCycle(): Lifecycle {
        return TestLifecycleOwner().lifecycle
    }

    @Provides
    fun provideLifetimeScope(lifecycle: Lifecycle): CoroutineScope {
        return lifecycle.coroutineScope
    }

    @Provides
    fun provideCoroutineDispatcherProvider(): AppCoroutineDispatchers {
        return TestDispatcherProvider(
            Dispatchers.Main, Dispatchers.IO, Dispatchers.IO, Dispatchers.Default,
            Executors.newFixedThreadPool(1).asCoroutineDispatcher()
        )
    }

    @Provides
    fun provideConnectivityChecker(
        @ApplicationContext context: Context,
        lifecycle: Lifecycle
    ): ConnectionDetector {
        return TestInternet()
    }

    class TestInternet(override val isInternetAvailable: Boolean = true) : ConnectionDetector
}
