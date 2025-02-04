package com.example.proyect.views.login.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.proyect.R
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.proyect.views.login.data.model.LoginUserRequest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(modifier: Modifier = Modifier,loginViewModel: LoginViewModel, navigateToRegister : () -> Unit , navigateToHome: () -> Unit) {
//val number:Int by loginViewModel.number.observeAsState(initial = 0)
    // Observa los datos del ViewModel
    val username: String by loginViewModel.username.observeAsState("")
    val password: String by loginViewModel.password.observeAsState("")

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))

        loginImage()

        Spacer(modifier = Modifier.height(40.dp))

        Inputs(
            username,
            password,
            loginViewModel
        )

        Spacer(modifier = Modifier.height(30.dp))

        Buttons(
            username,
            password,
            loginViewModel,
            navigateToHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        val annotatedText = buildAnnotatedString {
            append("¿No tienes cuenta?\n")
            pushStringAnnotation(tag = "register", annotation = "register")
            withStyle(
                style = SpanStyle(
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Regístrate acá")
            }
            pop()
        }

        ClickableText(
            text = annotatedText,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "register", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navigateToRegister()
                    }
            }
        )
    }
}


@Composable
fun loginImage(){
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "padlock",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .padding(2.dp)
        )
    }
}
@Composable
fun Inputs(username: String, password: String, loginViewModel: LoginViewModel) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = username,
        onValueChange = { loginViewModel.onChangeUsername(it) },
        label = { Text("Correo electronico") },
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text("Ingresa tu correo") },
        leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = "Usuario") },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                loginViewModel.viewModelScope.launch {
                    if (!focusState.isFocused && username.isNotEmpty()) {
                        Log.e("Data", "Ingreso")
                        loginViewModel.onFocusChanged()
                    }
                }
            },
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(20.dp))

    TextField(
        value = password,
        onValueChange = { loginViewModel.onChangePassword(it) },
        label = { Text("Contraseña") },
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text("Ingresa tu contraseña") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
    )
}

@Composable
fun Buttons(
    username: String,
    password: String,
    loginViewModel: LoginViewModel,
    navigateToHome: () -> Unit
) {
    val success by loginViewModel.success.observeAsState(initial = false)
    val token by loginViewModel.token.observeAsState()

    Button(
        onClick = {
            val user = LoginUserRequest(username, password)
            Log.d("Buttons", "Botón presionado con usuario: $username y contraseña: $password")
            loginViewModel.viewModelScope.launch {
                loginViewModel.onClick(user)
            }
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text("Entrar")
    }

    // Observa si el login fue exitoso y realiza la redirección

    LaunchedEffect(success) {
        if (success) {
            token?.let {
                loginViewModel.saveToken(it)
                navigateToHome()
            }
        }
    }
}
