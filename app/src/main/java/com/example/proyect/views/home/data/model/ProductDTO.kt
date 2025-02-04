package com.example.proyect.views.home.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    val idproduct: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String
)

