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

import app.template.base.util.interent.ConnectionDetector
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

const val HEADER_CACHE_CONTROL = "Cache-Control"
const val HEADER_PRAGMA = "Pragma"

class CacheInterceptor(private val maxCacheHours: Int) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val cacheControl = CacheControl.Builder()
            .maxAge(maxCacheHours, TimeUnit.HOURS)
            .build()

        val response = chain.proceed(chain.request())
        return response.newBuilder()
            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
            .removeHeader(HEADER_PRAGMA)
            .build()
    }
}

class OfflineCacheInterceptor(
    private val maxCacheHours: Int,
    private val internetChecker: ConnectionDetector
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!internetChecker.isInternetAvailable) {

            val cacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxStale(maxCacheHours, TimeUnit.HOURS)
                .build()

            val offlineRequest = chain.request().newBuilder()
                .cacheControl(cacheControl)
                .removeHeader(HEADER_PRAGMA)
                .build()
            return chain.proceed(offlineRequest)
        }
        return chain.proceed(chain.request())
    }
}
