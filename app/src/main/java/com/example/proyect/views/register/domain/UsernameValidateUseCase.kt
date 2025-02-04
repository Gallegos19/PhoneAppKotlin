package com.example.proyect.views.register.domain

import com.example.proyect.views.register.data.model.UsernameValidateDTO
import com.example.proyect.views.register.data.repository.RegisterService

class UsernameValidateUseCase {
    private  val repository = RegisterService()

    suspend operator fun invoke() : Result<UsernameValidateDTO> {
        val result  = repository.validateUsername()

        return result
    }
}