package com.example.proyect.views.map.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class mapViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> get() = _currentLocation

    @SuppressLint("MissingPermission") // Ensure permissions are checked in the activity/fragment
    fun fetchCurrentLocation() {
        val locationTask: Task<Location> = fusedLocationClient.lastLocation
        locationTask.addOnSuccessListener { location ->
            _currentLocation.postValue(location)
        }
        locationTask.addOnFailureListener {
            _currentLocation.postValue(null) // Handle failure gracefully
        }
    }
}
