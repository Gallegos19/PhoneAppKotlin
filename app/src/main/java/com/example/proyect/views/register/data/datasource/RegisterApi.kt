package com.example.proyect.views.register.data.datasource

import com.example.proyect.views.register.data.model.CreateUserRequest
import com.example.proyect.views.register.data.model.UserDTO
import com.example.proyect.views.register.data.model.UsernameValidateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RegisterApi {
    /*@GET("users/{username}")
    suspend fun validateUsername(@Path("username") username : String) : Response<UsernameValidateDTO>
    */
    @GET("v3/ada4ac3d-0946-4847-850a-b59e11c10e00")
    suspend fun validateUsername() : Response<UsernameValidateDTO>

    @POST("/users")
    suspend fun createUser(@Body request : CreateUserRequest) : Response<UserDTO>
}