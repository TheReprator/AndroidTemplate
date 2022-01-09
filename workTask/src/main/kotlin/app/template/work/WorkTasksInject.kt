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

package app.template.work

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import app.template.base.actions.WorkTasks
import app.template.base_android.appinitializers.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WorkTasksModule {
    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun workManagerConfig(
        threadPoolExecutor: ThreadPoolExecutor,
        workerFactory: HiltWorkerFactory
    ): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setExecutor(threadPoolExecutor)
            .build()
    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class WorkTasksModuleBinds {
    @Binds
    @IntoSet
    abstract fun provideWorkTasksInitializer(bind: WorkTasksInitializer): AppInitializer

    @Binds
    @Singleton
    abstract fun provideWorkActions(bind: WorkTasksImpl): WorkTasks
}
