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