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

package app.module.feature.location

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.template.base.actions.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val locationPermissionController: LocationController,
    private val logger: Logger,
    locationObserverFactory: LocationObserverFactory
) : ViewModel(), LocationStatusListener {
    companion object {
        private const val IS_STARTED_KEY = "is_started"
    }

    private val locationObserver: LocationObserver =
        locationObserverFactory.create(CompletableDeferred(this))

    private val _isStarted = MutableStateFlow(false)

    @SuppressLint("MissingPermission")
    val viewState = _isStarted
        .onEach { savedStateHandle.set(IS_STARTED_KEY, it) }
        .flatMapLatest { started ->
            if (!started) {
                flowOf(ViewState(isObservingLocation = false, currentLocation = null))
            } else {
                val granted = locationPermissionController.requestLocationPermission()
                if (granted) {
                    startObservingLocation()
                        .map { ViewState(isObservingLocation = true, currentLocation = it) }
                        .onStart { emit(ViewState(isObservingLocation = true)) }
                } else {
                    _isStarted.value = false
                    flowOf(ViewState(isObservingLocation = false, currentLocation = null))
                }
            }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ViewState()
        )

    init {
        _isStarted.value = savedStateHandle.get<Boolean>(IS_STARTED_KEY) ?: false
    }

    @SuppressLint("MissingPermission")
    private fun startObservingLocation(): Flow<Location> {
        return locationObserver.observeLocationUpdates()
    }

    fun toggleState() {
        _isStarted.update { !it }
    }

    data class ViewState(
        val isObservingLocation: Boolean = false,
        val currentLocation: Location? = null
    )

    override fun gpsUserAction(boolean: Boolean) {
        logger.d("1 gpsUserAction $boolean")
    }

    override fun gpsStatus(boolean: Boolean) {
        logger.d("2 gpsStatus $boolean")
    }

    override fun locationFetchStarted() {
        logger.d("3 locationFetchStarted ")
    }

    override fun latestLocation(location: Location) {
        logger.d("4 latestLocation $location")
    }

    override fun locationUnavailable() {
        logger.d("5 locationUnavailable")
    }
}