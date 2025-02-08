package com.example.proyect.views.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect.views.home.data.model.ProductDTO
import com.example.proyect.views.home.data.model.createProductRequest
import com.example.proyect.views.home.domain.GetProductsUseCase
import com.example.proyect.views.home.domain.createProductRepository
import com.example.proyect.views.home.domain.deleteProductoUseCase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val getProductsUseCase = GetProductsUseCase()
    private val repository = createProductRepository()
    private val deleteProduct = deleteProductoUseCase()

    var deleteSuccess by mutableStateOf(false)
        private set

    var success by mutableStateOf(false)
        private set

    private val _products = mutableStateOf<List<ProductDTO>>(emptyList())
    val products: List<ProductDTO> get() = _products.value

    private val _loading = mutableStateOf(false)
    val loading: Boolean get() = _loading.value

    private val _error = mutableStateOf("")
    val error: String get() = _error.value

    private val _message = mutableStateOf("")
    val message: String get() = _message.value

    private val _isModalVisible = mutableStateOf(false)
    val isModalVisible: Boolean get() = _isModalVisible.value

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = getProductsUseCase.getProducts()
                result.onSuccess { productList ->
                    _products.value = productList
                    _error.value = ""
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }

    fun addProduct(request: createProductRequest) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.createProducts(request)
                result.onSuccess { message ->
                    _message.value = message
                    success = true
                    fetchProducts()
                    Log.e("HomeViewModel", "Producto creado exitosamente")
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Error al crear el producto"
                    success = false
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al crear el producto"
                success = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = deleteProduct.deleteProduct(id)
                result.onSuccess {
                    _message.value = "Producto eliminado con éxito"
                    deleteSuccess = true
                    fetchProducts()
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Error al eliminar el producto"
                    deleteSuccess = false
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al eliminar el producto"
                deleteSuccess = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetDeleteSuccess() {
        deleteSuccess = false
    }

    fun resetSuccess() {
        success = false
    }
}
