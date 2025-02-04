package com.example.proyect.views.home.data.datasource

import com.example.proyect.views.home.data.model.ProductDTO
import com.example.proyect.views.home.data.model.ProductResponse
import com.example.proyect.views.home.data.model.createProductRequest
import com.example.proyect.views.login.data.model.LoginUserRequest
import com.example.proyect.views.login.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface HomeService {
    /*@GET("users/{username}")
    suspend fun validateUsername(@Path("username") username : String) : Response<UsernameValidateDTO>
    */
    //@GET("v3/ada4ac3d-0946-4847-850a-b59e11c10e00")
    //suspend fun validateUsername() : Response<UsernameValidateDTO>
    @Headers("Content-Type: application/json")
    @GET("/products")
    suspend fun getProducts(): Response<List<ProductDTO>>


    @Headers("Content-Type: application/json")
    @POST("/products")
    suspend fun createProduct(@Body request: createProductRequest): Response<String>

}