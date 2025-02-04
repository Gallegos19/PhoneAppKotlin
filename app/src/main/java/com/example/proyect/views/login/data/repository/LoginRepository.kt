package com.example.proyect.views.login.data.repository

import android.annotation.SuppressLint
import com.example.proyect.core.network.RetrofitHelper
import com.example.proyect.views.login.data.model.LoginUserRequest
import com.example.proyect.views.login.data.model.UserDTO

class LoginRepository {
    @SuppressLint("SuspiciousIndentation")
    suspend fun loginUser(user: LoginUserRequest): Result<UserDTO> {
        val loginService = RetrofitHelper.getRetrofitLogin()

        return try {
            val response = loginService.loginUser(user)
                if (response.isSuccessful) {
                    val userDTO = response.body()
                    if (userDTO != null) {
                        Result.success(userDTO)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    Result.failure(Exception("Error en la respuesta: ${response.code()} - ${response.message()}"))
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

