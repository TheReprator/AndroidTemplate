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

package app.module.modulea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.template.base.extensions.computationalBlock
import app.template.base.util.AppCoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import io.americanexpress.busybee.BusyBee
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class ModuleAViewModal @Inject constructor(
    private val coroutineDispatcherProvider: AppCoroutineDispatchers
) : ViewModel() {

    private val BUSYBEE_OPERATION_NAME = "Network Call"
    private val busyBee = BusyBee.singleton()

    fun fetchList() {
        useCaseCall(
            {
            },
            {
            }
        )
    }

    fun retryList() {
        fetchList()
    }

    fun onRefresh() {
        useCaseCall(
            {
            },
            {
            }
        )
    }

    private fun useCaseCall(
        blockLoader: (Boolean) -> Unit,
        blockError: (String) -> Unit
    ) {
        busyBee.busyWith(BUSYBEE_OPERATION_NAME)

        computationalBlock {
            busyBee.completed(BUSYBEE_OPERATION_NAME)
        }
    }

    private fun computationalBlock(
        coroutineExceptionHandler: CoroutineExceptionHandler? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.computationalBlock(
            coroutineDispatcherProvider,
            coroutineExceptionHandler,
            block
        )
    }
}
