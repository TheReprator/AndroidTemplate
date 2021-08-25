/*
 * Copyright 2021 Vikram LLC
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

package reprator.module.modulea

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.template.base.extensions.computationalBlock
import app.template.base.util.network.AppCoroutineDispatchers
import app.template.base_android.util.event.Event
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

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData("")
    val errorMsg: LiveData<String> = _errorMsg

    private val _swipeErrorMsg = MutableLiveData(Event(""))
    val swipeErrorMsg: LiveData<Event<String>> = _swipeErrorMsg

    private val _swipeLoading = MutableLiveData(false)
    val swipeLoading: LiveData<Boolean> = _swipeLoading

    var previousPosition = -1

    fun fetchList() {
        useCaseCall(
            {
                _isLoading.value = it
            },
            {
                _errorMsg.value = it
            }
        )
    }

    fun retryList() {
        _isLoading.value = true
        _errorMsg.value = ""
        fetchList()
    }

    fun onRefresh() {
        useCaseCall(
            {
                _swipeLoading.value = it
            },
            {
                _swipeErrorMsg.value = Event(it)
            }
        )
    }

    private fun useCaseCall(
        blockLoader: (Boolean) -> Unit,
        blockError: (String) -> Unit
    ) {
        busyBee.busyWith(BUSYBEE_OPERATION_NAME)

        computationalBlock {

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
