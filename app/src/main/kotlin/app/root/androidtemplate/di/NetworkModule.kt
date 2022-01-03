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
import app.template.base.util.interent.ConnectionDetector
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CONNECTION_TIME = 20L
private const val CACHE_SIZE = (50 * 1024 * 1024).toLong()
private const val CACHE_VALID_HOURS = 2

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        JackSonModule::class
    ]
)
object NetworkModule {

    @Provides
    fun provideCacheInterceptor(): CacheInterceptor =
        CacheInterceptor(CACHE_VALID_HOURS)

    @Provides
    fun provideOfflineCacheInterceptor(connectionDetector: ConnectionDetector): OfflineCacheInterceptor =
        OfflineCacheInterceptor(
            CACHE_VALID_HOURS,
            connectionDetector
        )

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        offlineCacheInterceptor: OfflineCacheInterceptor,
        cacheInterceptor: CacheInterceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                connectTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                readTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                writeTimeout(CONNECTION_TIME, TimeUnit.SECONDS)
                followRedirects(true)
                followSslRedirects(true)
                cache(cache)
                retryOnConnectionFailure(false)
                addInterceptor(httpLoggingInterceptor)
                addInterceptor(offlineCacheInterceptor)
                addNetworkInterceptor(cacheInterceptor)
            }
            .build()
    }

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
