package com.example.proyect.views.map.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MapViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(mapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return mapViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
