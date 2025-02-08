package com.example.proyect.views.home.domain

import com.example.proyect.views.home.data.repository.getProductRepository

class deleteProductoUseCase {
    private  val repository = getProductRepository()

    suspend fun deleteProduct(id: Int) : Result<String>  {
        val result = repository.deleteProduct(id)
        //En caso de existir acá debe estar la lógica de negocio
        return result
    }
}