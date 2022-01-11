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

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import app.template.base.actions.WorkTasks
import javax.inject.Inject

class WorkTasksImpl @Inject constructor(
    private val workManager: WorkManager
) : WorkTasks {
    override fun workManagerSample() {
        val request = OneTimeWorkRequestBuilder<WorkManagerSample>()
            .addTag(WorkManagerSample.TAG)
            .setInputData(WorkManagerSample.buildData(-1))
            .build()
        workManager.enqueue(request)
    }
}
