package com.example.proyect.views.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val message: String,
    val username : String,
    val token : String
)
