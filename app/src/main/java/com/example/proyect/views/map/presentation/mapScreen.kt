package com.example.proyect.views.map.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mapViewModel: mapViewModel,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val locationPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted.value = isGranted
        if (isGranted) {
            mapViewModel.fetchCurrentLocation()
        }
    }

    LaunchedEffect(locationPermissionGranted.value) {
        if (locationPermissionGranted.value) {
            mapViewModel.fetchCurrentLocation()
        } else {
            locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val currentLocation by mapViewModel.currentLocation.observeAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            currentLocation?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(19.4326, -99.1332), // CDMX por defecto
            12f
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mapa", color = Color.DarkGray) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.Logout, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = locationPermissionGranted.value),
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    currentLocation?.let { location ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(location.latitude, location.longitude)
                            ),
                            title = "Tu ubicación actual"
                        )
                    }
                }
            }
        }
    )
}
