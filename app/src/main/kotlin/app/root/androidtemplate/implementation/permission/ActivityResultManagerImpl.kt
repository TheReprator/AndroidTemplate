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

package app.root.androidtemplate.implementation.permission

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import app.template.base_android.permission.ActivityResultManager
import app.template.base_android.util.ActivityProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.coroutines.resume

class ActivityResultManagerImpl @Inject constructor(private val activityProvider: ActivityProvider) :
    ActivityResultManager {

    companion object {
        private const val SAVED_STATE_REGISTRY_KEY = "activityresult_saved_state"
        private const val PENDING_RESULT_KEY = "pending"
        private const val LAST_INCREMENT_KEY = "key_increment"
    }

    private val keyIncrement = AtomicInteger(0)
    private var pendingResult: String? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun <I, O, C : ActivityResultContract<I, O>> requestResult(
        contract: C,
        input: I
    ): O? {
        var (isLaunched, key) = activityProvider.currentActivity?.calculateKey(contract)
            ?: return null

        pendingResult = contract.javaClass.simpleName
        return activityProvider.activityFlow
            .mapLatest { currentActivity ->
                if (!isLaunched) {
                    currentActivity.prepareSavedData()
                }

                var launcher: ActivityResultLauncher<I>? = null
                try {
                    suspendCancellableCoroutine<O> { continuation ->
                        launcher = currentActivity.activityResultRegistry.register(
                            key,
                            contract
                        ) { result ->
                            pendingResult = null
                            currentActivity.clearSavedStateData()
                            continuation.resume(result)
                        }

                        if (!isLaunched) {
                            launcher!!.launch(input)
                            isLaunched = true
                        }
                    }
                } finally {
                    launcher?.unregister()
                }
            }
            .first()
    }

    private fun <C : ActivityResultContract<*, *>> ComponentActivity.calculateKey(contract: C): Pair<Boolean, String> {
        fun generateKey(increment: Int) = "result_$increment"

        val savedBundle = savedStateRegistry.consumeRestoredStateForKey(SAVED_STATE_REGISTRY_KEY)

        return if (contract.javaClass.simpleName == savedBundle?.getString(PENDING_RESULT_KEY)) {
            Pair(true, generateKey(savedBundle!!.getInt(LAST_INCREMENT_KEY)))
        } else {
            Pair(false, generateKey(keyIncrement.getAndIncrement()))
        }
    }

    private fun ComponentActivity.prepareSavedData() {
        savedStateRegistry.registerSavedStateProvider(
            SAVED_STATE_REGISTRY_KEY
        ) {
            bundleOf(
                PENDING_RESULT_KEY to pendingResult,
                LAST_INCREMENT_KEY to keyIncrement.get() - 1
            )
        }
    }

    private fun ComponentActivity.clearSavedStateData() {
        savedStateRegistry.unregisterSavedStateProvider(
            SAVED_STATE_REGISTRY_KEY
        )
        // Delete the data by consuming it
        savedStateRegistry.consumeRestoredStateForKey(
            SAVED_STATE_REGISTRY_KEY
        )
    }
}