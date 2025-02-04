package com.example.proyect.views.register.domain


import com.example.proyect.views.register.data.model.CreateUserRequest
import com.example.proyect.views.register.data.model.UserDTO
import com.example.proyect.views.register.data.model.UsernameValidateDTO
import com.example.proyect.views.register.data.repository.RegisterService

class CreateUserUSeCase {
    private  val repository = RegisterService()

    suspend operator fun invoke(user: CreateUserRequest) : Result<UserDTO> {
        val result = repository.createUser(user)

        //En caso de existir acá debe estar la lógica de negocio
        return result
    }
}