package com.example.proyect.views.login.data.datasource

import com.example.proyect.core.navigation.Login
import com.example.proyect.views.login.data.model.LoginUserRequest
import com.example.proyect.views.login.data.model.UserDTO
//import com.example.proyect.views.login.data.model.UsernameValidateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {
    /*@GET("users/{username}")
    suspend fun validateUsername(@Path("username") username : String) : Response<UsernameValidateDTO>
    */
    //@GET("v3/ada4ac3d-0946-4847-850a-b59e11c10e00")
    //suspend fun validateUsername() : Response<UsernameValidateDTO>

    @POST("/login")
    suspend fun loginUser(@Body request : LoginUserRequest) : Response<UserDTO>
}