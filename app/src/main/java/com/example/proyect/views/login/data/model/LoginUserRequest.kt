package com.example.proyect.views.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserRequest(
    val correo : String,
    val contraseña : String
)
