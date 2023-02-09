
package com.eyeta.client

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class GooglePositionProvider(context: Context, listener: PositionListener) : PositionProvider(context, listener) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @Suppress("DEPRECATION", "MissingPermission")
    override fun startUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.priority = getPriority(preferences.getString(MainFragment.KEY_ACCURACY,"medium"))
        locationRequest.interval = if (distance > 0 || angle > 0) MINIMUM_INTERVAL else interval
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun stopUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun requestSingleLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                listener.onPositionUpdate(Position(deviceId, location, getBatteryStatus(context)))
            }
        }
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                processLocation(location)
            }
        }
    }

    private fun getPriority(accuracy: String?): Int {
        return when (accuracy) {
            "high" -> LocationRequest.PRIORITY_HIGH_ACCURACY
            "low"  -> LocationRequest.PRIORITY_LOW_POWER
            else   -> LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }
}
