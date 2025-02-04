package com.example.proyect.views.home.data.model

import kotlinx.serialization.Serializable

@Serializable
data class createProductRequest(
    val nombre : String,
    val descripcion: String,
    val precio: Float,
    val imagen: String
)
