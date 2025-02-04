package com.example.proyect.views.login.domain

import com.example.proyect.views.login.data.model.LoginUserRequest
import com.example.proyect.views.login.data.repository.LoginRepository
import com.example.proyect.views.register.data.model.CreateUserRequest
import com.example.proyect.views.login.data.model.UserDTO
import com.example.proyect.views.register.data.repository.RegisterService

class LoginUserUseCase {
    private val repository = LoginRepository()

    suspend operator fun invoke(user: LoginUserRequest): Result<UserDTO> {
        return repository.loginUser(user)
    }
}
