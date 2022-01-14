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

package app.module.modulea.permission

import android.Manifest
import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Looper
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import app.template.base.util.Logger
import app.template.base_android.permission.ActivityResultManager
import app.template.base_android.util.ActivityProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class LocationObserver @AssistedInject constructor(
    @Assisted
    private val locationStatusListener: CompletableDeferred<LocationStatusListener>,
    private val activityProvider: ActivityProvider,
    private val activityResultManager: ActivityResultManager,
    private val logger: Logger,
    private val coroutineScope: CoroutineScope
) {

    companion object {
        private const val UPDATE_INTERVAL = 1000L
        private const val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun observeLocationUpdates(): Flow<Location> {
        return callbackFlow {
            logger.e("observing location updates")
            locationStatusListener.await().locationFetchStarted()

            if (null == activityProvider.currentActivity)
                throw Exception("CurrentActivity is null")

            val client =
                LocationServices.getFusedLocationProviderClient(activityProvider.currentActivity!!)

            val locationRequest = getLocationRequest()

            configureSettingClient(locationRequest)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    logger.e("onLocationResult ${locationResult.lastLocation}")
                    trySend(locationResult.lastLocation)
                    coroutineScope.launch {
                        locationStatusListener.await().latestLocation(locationResult.lastLocation)
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    logger.e("onLocationAvailability ${locationAvailability.isLocationAvailable}")
                    super.onLocationAvailability(locationAvailability)
                    coroutineScope.launch {
                        locationStatusListener.await().locationUnavailable()
                    }
                }
            }

            client.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                logger.e("stop observing location updates")
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun getLocationRequest(): LocationRequest {
        return LocationRequest
            .create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL)
    }

    private fun configureSettingClient(locationRequest: LocationRequest) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val settingClient = LocationServices.getSettingsClient(activityProvider.currentActivity!!)
        settingClient.checkLocationSettings(builder.build()).addOnSuccessListener {
            coroutineScope.launch {
                locationStatusListener.await().gpsStatus(true)
            }
            logger.e("addOnSuccessListener")
            val states: LocationSettingsStates? = it.locationSettingsStates
            logger.e("GPS Present: " + states?.isGpsPresent)
            logger.e("Location Present: " + states?.isLocationPresent)
            logger.e("GPS Usable: " + states?.isGpsUsable)
            logger.e("Location Usable: " + states?.isLocationUsable)
        }.addOnFailureListener { e ->
            logger.e("fail addOnFailureListener")
            coroutineScope.launch {
                locationStatusListener.await().gpsStatus(false)
            }
            if (e is ResolvableApiException) {
                try {
                    launchGpsDialog(e)
                } catch (sendEx: SendIntentException) {
                    logger.e("fail 1 addOnFailureListener")
                } catch (ex: NullPointerException) {
                    logger.e("fail 2 addOnFailureListener")
                }
            }
        }
    }

    private fun launchGpsDialog(e: ResolvableApiException) =
        coroutineScope.launch {
            val result = activityResultManager.requestResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                input = IntentSenderRequest.Builder(e.resolution).build()
            ) ?: return@launch

            if (Activity.RESULT_OK == result.resultCode) {
                logger.e("result ok")
                locationStatusListener.await().gpsUserAction(true)
            } else {
                logger.e("result cancel")
                locationStatusListener.await().gpsUserAction(false)
            }
        }

}