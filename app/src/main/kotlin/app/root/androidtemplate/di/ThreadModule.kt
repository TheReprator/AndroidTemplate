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

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

/*
https://chao2zhang.medium.com/reduce-reuse-recycle-your-thread-pools-%EF%B8%8F-81e2f54d8a1d
* */
@InstallIn(SingletonComponent::class)
@Module()
object ThreadModule {

    @Provides
    @Singleton
    fun threadPoolExecutor(): ThreadPoolExecutor {
        val index = AtomicInteger()
        return ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors().coerceAtLeast(2),
            Int.MAX_VALUE,
            60,
            TimeUnit.SECONDS,
            SynchronousQueue(),
            ThreadFactory { runnable ->
                Thread(runnable, "Shared Thread Pool ${index.incrementAndGet()}")
            }
        )
    }
}
