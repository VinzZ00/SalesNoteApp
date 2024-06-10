package com.example.salesapp.drivers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class locationDriver(private var context : Context) {

    private val _locationFlow = MutableStateFlow("Unknown Location")
    val locationFlow = _locationFlow

    private var job : Job? = null

    fun startUpdate() {
        job = (context as ComponentActivity).lifecycleScope.launch {
            while (isActive) {
                val locationData = withContext(Dispatchers.IO) {
                    // Call the suspend fun
                    getCurrentLocation(context)
                }

                _locationFlow.value = locationData
                delay(10000)
            }
        }
    }

    suspend fun getCurrentLocation(context : Context) : String {
        return try {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            var locationTask : Task<Location>? = null
            if (checkLocationPermission(context)) {
                locationTask = fusedLocationProviderClient.lastLocation
                val location = withContext(Dispatchers.IO) {
                    Tasks.await(locationTask)
                }
                if (location != null) {
                    "Lat: ${location.latitude}, Lon: ${location.longitude}"
                } else {
                    "Location not found"
                }
            } else {
                // Request Permission
                ActivityCompat.requestPermissions(
                    context as ComponentActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )

                // Recheck after request permission
                if (checkLocationPermission(context)) {
                    getCurrentLocation(context)
                } else {
                    "Location Permission Denied"
                }
            }
        } catch (e : Exception) {
            "Error: ${e.message}"
        }
    }

    fun checkLocationPermission(context : Context) : Boolean = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
}