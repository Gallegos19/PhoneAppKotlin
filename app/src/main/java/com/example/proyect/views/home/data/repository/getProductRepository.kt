package com.example.proyect.views.home.data.repository

import com.example.proyect.core.network.RetrofitHelper
import com.example.proyect.views.home.data.model.ProductDTO
import com.example.proyect.views.home.data.model.ProductResponse
import com.example.proyect.views.home.data.model.createProductRequest
import com.example.proyect.views.login.data.model.LoginUserRequest
import com.example.proyect.views.login.data.model.UserDTO

class getProductRepository {
    private val homeService = RetrofitHelper.getRetrofitHome()

    suspend fun createProduct(request: createProductRequest): Result<String> {
        return try {
            val response = homeService.createProduct(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it) // Devuelve el mensaje del servidor
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProducts(): Result<List<ProductDTO>> {
        return try {
            val response = homeService.getProducts()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
