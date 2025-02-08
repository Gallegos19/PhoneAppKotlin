package com.example.proyect.core.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.proyect.views.map.presentation.MapScreen
import com.example.proyect.views.map.presentation.MapViewModelFactory
import com.example.proyect.views.map.presentation.mapViewModel

@Composable
fun NavigationWrapper(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                modifier,
                LoginViewModel(),
                navigateToRegister = { navController.navigate(Register) },
                navigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Register> {
            RegisterContent(
                modifier,
                RegisterViewModel()
            ) { navController.navigate(Login) }
        }
        composable<Home> {
            HomeScreen(
                HomeViewModel(),
                navigateToLogin = { navController.navigate(Login) },
                navigateToMap = { navController.navigate(Map) }
            )
        }
        composable<Map> {
            val context = LocalContext.current.applicationContext
            val mapViewModel: mapViewModel = viewModel(factory = MapViewModelFactory(context as Application))

            MapScreen(
                mapViewModel = mapViewModel,
                navigateBack = { navController.navigate(Home) }
            )
        }
    }
}
