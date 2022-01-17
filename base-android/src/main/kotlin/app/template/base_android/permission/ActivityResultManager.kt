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

package app.template.base_android.permission

import androidx.activity.result.contract.ActivityResultContract

interface ActivityResultManager {

    /**
     * Requests an Activity Result using the current visible Activity from the app.
     *
     * To allow handling process-death scenarios, the function checks if there is a pending result before
     * re-requesting a new one. So to handle this case, you just need to remember that there is a pending operation
     * in your [androidx.lifecycle.ViewModel] using a [androidx.lifecycle.SavedStateHandle],
     * then call the function another time when the app recovers to continue from where it left.
     *
     * @param contract the [androidx.activity.result.contract.ActivityResultContract] to use
     * @param input the input to pass when requesting the result, it needs to match the used [contract]
     *
     * @return the activity result
     */
    suspend fun <I, O, C : ActivityResultContract<I, O>> requestResult(contract: C, input: I): O?
}
