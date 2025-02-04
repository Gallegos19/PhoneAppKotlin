package com.example.proyect.core.network

import com.example.proyect.views.home.data.datasource.HomeService
import com.example.proyect.views.login.data.datasource.LoginService
import com.example.proyect.views.register.data.datasource.RegisterApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "http://192.168.1.11:8080"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val service = retrofit.create(LoginService::class.java)

    fun getRetrofit() : RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }
    fun getRetrofitLogin() : LoginService {
        return retrofit.create(LoginService::class.java)
    }
    fun getRetrofitHome() : HomeService {
        return retrofit.create(HomeService::class.java)
    }
}