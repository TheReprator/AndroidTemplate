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
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import app.template.base.util.Logger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class WorkManagerSample @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val logger: Logger
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "sync-show-watched-episodes"
        private const val PARAM_SHOW_ID = "show-id"

        fun buildData(showId: Long) = Data.Builder()
            .putLong(PARAM_SHOW_ID, showId)
            .build()
    }

    override suspend fun doWork(): Result {
        val showId = inputData.getLong(PARAM_SHOW_ID, -1)
        logger.e("$TAG worker running for show id: $showId")
        return Result.success()
    }
}