package com.example.proyect.views.home.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect.views.home.data.model.ProductDTO
import com.example.proyect.views.home.data.model.createProductRequest
import com.example.proyect.views.home.data.repository.getProductRepository
import com.example.proyect.views.home.domain.GetProductsUseCase
import com.example.proyect.views.home.domain.createProductRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val getProductsUseCase = GetProductsUseCase()
    private val repository = createProductRepository()


    private val _products = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>(false)
    val success: LiveData<Boolean> = _success

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    // Estado para controlar la visibilidad del modal
    private val _isModalVisible = MutableLiveData<Boolean>(false)
    val isModalVisible: LiveData<Boolean> = _isModalVisible

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

    // Función para mostrar el modal
    fun showModal() {
        _isModalVisible.value = true
    }

    // Función para ocultar el modal
    fun hideModal() {
        _isModalVisible.value = false
    }

    fun addProduct(request: createProductRequest) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.createProducts(request)
                result.onSuccess { message ->
                    _success.value = true
                    _message.value = message // Almacena el mensaje del servidor
                    fetchProducts() // Actualiza la lista de productos
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Error al crear el producto"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al crear el producto"
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetSuccess() {
        _success.value = false
    }
}
