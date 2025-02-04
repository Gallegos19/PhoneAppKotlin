package com.example.proyect.views.login.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect.views.login.data.datasource.LoginService
import com.example.proyect.views.login.data.model.LoginUserRequest
import com.example.proyect.views.login.data.repository.LoginRepository
import com.example.proyect.views.register.data.model.CreateUserRequest
import com.example.proyect.views.register.domain.CreateUserUSeCase
import com.example.proyect.views.register.domain.UsernameValidateUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

import kotlin.jvm.Throws

class LoginViewModel : ViewModel() {
    private val usernameUseCase = UsernameValidateUseCase()
    private val createUseCase = CreateUserUSeCase()

    private var _username = MutableLiveData<String>()
    val username : LiveData<String> = _username

    private var _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private var _success = MutableLiveData<Boolean>(false)
    val success : LiveData<Boolean> = _success

    private var _error = MutableLiveData<String>("")
    val error : LiveData<String> = _error

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun onClick(user: LoginUserRequest) {
        viewModelScope.launch {
            Log.d("LoginViewModel", "onClick iniciado con usuario: ${user.correo} y contraseña ${user.contraseña}")
            val result = LoginRepository().loginUser(user)
            result.onSuccess { userDTO ->
                Log.d("LoginViewModel", "Login exitoso: Token recibido")
                _success.value = true
                _error.value = ""
                _token.value = userDTO.token
                Log.d("LoginViewModel","Token: ${token.value}")
                Log.d("LoginViewModel","success: ${success.value}")

            }.onFailure { exception ->
                Log.e("LoginViewModel", "Login fallido: ${exception.message}")
                _success.value = false
                _error.value = exception.message ?: "Error desconocido"
            }
        }
    }



    fun saveToken(token: String){
        _token.value = token
    }

    fun onChangeUsername(username : String) {
        _username.value = username
    }

    fun onChangePassword (password : String) {
        _password.value = password
    }

    suspend fun onFocusChanged() {
        viewModelScope.launch {
            val result = usernameUseCase()
            result.onSuccess {
                    data -> (
                    if (data.success) {
                        _success.value = data.success
                        _error.value = ""
                    }
                    else
                        _error.value = "El username ya existe"
                    )
            }.onFailure {
                    exception -> _error.value = exception.message ?: "Error desconocido"
            }
        }
    }


}