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

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import app.root.androidtemplate.BuildConfig
import app.root.androidtemplate.R
import app.root.androidtemplate.implementation.AppCoroutineDispatchersImpl
import app.root.androidtemplate.implementation.DateUtilsImpl
import app.root.androidtemplate.implementation.connectivity.InternetChecker
import app.template.base.util.AppCoroutineDispatchers
import app.template.base.util.date.DateUtils
import app.template.base.util.interent.ConnectionDetector
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideLifeCycle(): Lifecycle {
        return ProcessLifecycleOwner.get().lifecycle
    }

    @Singleton
    @Provides
    fun providesCoroutineScope(appCoroutineDispatchers: AppCoroutineDispatchers): CoroutineScope {
        return CoroutineScope(SupervisorJob() + appCoroutineDispatchers.default)
    }

    @Provides
    fun provideCoroutineDispatcherProvider(threadPoolExecutor: ThreadPoolExecutor): AppCoroutineDispatchers {
        return AppCoroutineDispatchersImpl(
            Dispatchers.Main, threadPoolExecutor.asCoroutineDispatcher()
        )
    }

    @Provides
    fun provideAppPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    @Provides
    fun provideConnectivityChecker(
        @ApplicationContext context: Context,
        lifecycle: Lifecycle
    ): ConnectionDetector {
        return InternetChecker(context, lifecycle)
    }

    @Provides
    fun provideDateUtils(): DateUtils {
        return DateUtilsImpl()
    }

    @Named("isDebug")
    @Provides
    fun provideIsDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
}
