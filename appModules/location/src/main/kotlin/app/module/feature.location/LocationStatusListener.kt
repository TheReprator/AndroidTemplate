package app.module.feature.location

import android.location.Location

interface LocationStatusListener {
    fun gpsUserAction(boolean: Boolean)
    fun gpsStatus(boolean: Boolean)
    fun locationFetchStarted()
    fun latestLocation(location: Location)
    fun locationUnavailable()
}