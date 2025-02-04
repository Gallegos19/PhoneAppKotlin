package com.example.proyect.views.home.domain

import com.example.proyect.views.home.data.model.ProductResponse
import com.example.proyect.views.home.data.model.createProductRequest
import com.example.proyect.views.home.data.repository.getProductRepository

class createProductRepository {
    private  val repository = getProductRepository()

    suspend fun createProducts(request: createProductRequest) : Result<String>  {
        val result = repository.createProduct(request)
        return result
    }
}