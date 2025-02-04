package com.example.proyect.views.register.data.model

data class CreateUserRequest(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contraseña: String
)