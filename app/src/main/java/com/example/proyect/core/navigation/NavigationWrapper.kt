package com.example.proyect.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyect.views.login.presentation.LoginScreen
import com.example.proyect.views.login.presentation.LoginViewModel
import com.example.proyect.views.register.presentation.RegisterContent
import com.example.proyect.views.register.presentation.RegisterViewModel
import com.example.proyect.core.navigation.Login
import com.example.proyect.core.navigation.Register
import com.example.proyect.views.home.presentation.HomeScreen
import com.example.proyect.views.home.presentation.HomeViewModel


@Composable
fun NavigationWrapper(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> { LoginScreen(modifier,LoginViewModel(), navigateToRegister = {navController.navigate(Register)}, navigateToHome = {navController.navigate(Home){
            popUpTo(Login) { inclusive = true }
            launchSingleTop = true
        } })}
        composable<Register> { RegisterContent(modifier, RegisterViewModel()) {navController.navigate(Login)} }
        composable<Home> { HomeScreen(HomeViewModel()) {navController.navigate(Login)}}
    }
}