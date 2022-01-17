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
import app.root.androidtemplate.BuildConfig
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.File
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CONNECTION_TIME = 20L
private const val CACHE_SIZE = (50 * 1024 * 1024).toLong()

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        JackSonModule::class
    ]
)
object NetworkModule {
    @Provides
    fun provideCache(file: File): Cache {
        return Cache(file, CACHE_SIZE)
    }

    @Provides
    fun provideFile(
        @ApplicationContext context: Context
    ): File {
        return File(context.cacheDir, "cache_androidTemplate")
    }

    @Provides
    fun provideOkHttpClient(
        threadPoolExecutor: ThreadPoolExecutor,
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        cache: Cache,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                cache(cache)
                connectTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                readTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                writeTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                followRedirects(true)
                followSslRedirects(true)
                retryOnConnectionFailure(false)
                interceptors.forEach(::addInterceptor)
                connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
                dispatcher(Dispatcher(threadPoolExecutor))
            }
            .build()
    }

    @Singleton
    @Provides
    fun createRetrofit(
        okHttpClient: Lazy<OkHttpClient>,
        converterFactory: JacksonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl())
            .client(okHttpClient.get())
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun baseUrl() = BuildConfig.HOST
}
