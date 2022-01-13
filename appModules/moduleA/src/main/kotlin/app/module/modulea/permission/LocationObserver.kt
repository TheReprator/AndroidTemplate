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
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Looper
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.EXTRA_INTENT_SENDER_REQUEST
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.template.base.util.Logger
import app.template.base_android.permission.ActivityResultManager
import app.template.base_android.util.ActivityProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LocationObserver @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val activityResultManager: ActivityResultManager,
    private val logger: Logger
) {

    companion object {
        const val REQUEST_CHECK_SETTING = 10011
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun observeLocationUpdates(): Flow<Location> {
        return callbackFlow {
            logger.e("observing location updates")

            if (null == activityProvider.currentActivity)
                throw Exception("CurrentActivity is null")

            val currentFragment =
                (activityProvider.currentActivity!! as AppCompatActivity).supportFragmentManager.fragments.last()
            logger.e("name ${currentFragment}")

            val client =
                LocationServices.getFusedLocationProviderClient(activityProvider.currentActivity!!)

            val locationRequest = getLocationRequest()

            configureSettingClient(locationRequest)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    logger.e("onLocationResult ${locationResult.lastLocation}")
                    trySend(locationResult.lastLocation)
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    logger.e("onLocationAvailability ${locationAvailability.isLocationAvailable}")
                    super.onLocationAvailability(locationAvailability)
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
            .setInterval(1000)
            .setFastestInterval(500)
    }

    private suspend fun configureSettingClient(locationRequest: LocationRequest) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val settingClient = LocationServices.getSettingsClient(activityProvider.currentActivity!!)
        settingClient.checkLocationSettings(builder.build()).addOnSuccessListener {
            logger.e("addOnSuccessListener")
            val states: LocationSettingsStates? = it.locationSettingsStates
            logger.e("GPS Present: " + states?.isGpsPresent)
            logger.e("Location Present: " + states?.isLocationPresent)
            logger.e("GPS Usable: " + states?.isGpsUsable)
            logger.e("Location Usable: " + states?.isLocationUsable)
        }.addOnFailureListener { e ->
            logger.e("fail addOnFailureListener")
            if (e is ResolvableApiException) {
                try {
                    activityProvider.currentActivity?.lifecycleScope?.launch {
                        launchGpsOnNotification(e)
                    }
                } catch (sendEx: SendIntentException) {
                    logger.e("fail 1 addOnFailureListener")
                } catch (ex: NullPointerException) {
                    logger.e("fail 2 addOnFailureListener")
                }
            }
        }
    }

    private suspend fun launchGpsOnNotification(e: ResolvableApiException) {
        val result = activityResultManager.requestResult(
            contract = LocationContract(),
            input = e
        )
    }

    private class LocationContract :
        ActivityResultContract<ResolvableApiException, ActivityResult>() {
        override fun createIntent(context: Context, input: ResolvableApiException): Intent {
            return Intent(ACTION_INTENT_SENDER_REQUEST)
                .putExtra(
                    EXTRA_INTENT_SENDER_REQUEST,
                    IntentSenderRequest.Builder(input.resolution).build()
                )
        }

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult {
            val data = if (resultCode == RESULT_OK)
                0 else 1
            Timber.e("parse $data")
            return ActivityResult(resultCode, intent)
        }
    }
}