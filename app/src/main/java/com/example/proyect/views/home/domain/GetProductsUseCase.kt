package com.example.proyect.views.home.domain

import com.example.proyect.views.home.data.model.ProductDTO
import com.example.proyect.views.home.data.repository.getProductRepository

class GetProductsUseCase {
    private  val repository = getProductRepository()

    suspend fun getProducts() : Result<List<ProductDTO>>  {
        val result = repository.getProducts()
        //En caso de existir acá debe estar la lógica de negocio
        return result
    }
}