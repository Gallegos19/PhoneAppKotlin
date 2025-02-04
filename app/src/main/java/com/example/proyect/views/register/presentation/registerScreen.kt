package com.example.proyect.views.register.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.proyect.R
import com.example.proyect.views.register.data.model.CreateUserRequest
import kotlinx.coroutines.launch

@Composable
fun RegisterContent(modifier: Modifier = Modifier, registerViewModel: RegisterViewModel, navigateToLogin : () -> Unit ) {
    // Observa los datos del ViewModel
    val username: String by registerViewModel.nombre.observeAsState("")
    val apellido: String by registerViewModel.apellido.observeAsState("")
    val correo: String by registerViewModel.correo.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")
    val success: Boolean by registerViewModel.success.observeAsState(false)

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Black, Color.Blue),
        startY = 0f,
        endY = 9900f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Registrar Cuenta",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))

        padlock()

        Spacer(modifier = Modifier.height(40.dp))

        Inputs(username, apellido, correo, password, registerViewModel)

        Spacer(modifier = Modifier.height(30.dp))

        Buttons(username, apellido, correo, password, registerViewModel, navigateToLogin)
        Spacer(modifier = Modifier.height(30.dp))

        val annotatedText = buildAnnotatedString {
            append("¿Ya tienes cuenta?\n")
            pushStringAnnotation(tag = "register", annotation = "register")
            withStyle(
                style = SpanStyle(
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Inicia Sesión aqui")
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
                        navigateToLogin()
                    }
            }
        )
    }
}


@Composable
fun padlock(){
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.padlock),
            contentDescription = "padlock",
            modifier = Modifier
                .size(200.dp) // Usa size en lugar de width y height
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .padding(2.dp)
        )
    }
}

@Composable
fun Inputs(username: String, apellido: String, correo: String, password: String, registerViewModel: RegisterViewModel) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = username,
        onValueChange = { registerViewModel.onChangeNombre(it) },
        label = { Text("Nombres") },
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text("Ingresa tu nombre") },
        leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = "Usuario") },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                registerViewModel.viewModelScope.launch {
                    if (!focusState.isFocused && username.isNotEmpty()) {
                        Log.e("Data", "Ingreso")
                    }
                }
            },
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(20.dp))
    TextField(
        value = apellido,
        onValueChange = { registerViewModel.onChangeApellido(it) },
        label = { Text("Apellidos") },
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text("Ingresa tu apellido") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Apellido") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(20.dp))
    TextField(
        value = correo,
        onValueChange = { registerViewModel.onChangeCorreo(it) },
        label = { Text("Correo electronico") },
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text("Ingresa tu correo") },
        leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = "Usuario") },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                registerViewModel.viewModelScope.launch {
                    if (!focusState.isFocused && username.isNotEmpty()) {
                        Log.e("Data", "Ingreso")
                    }
                }
            },
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(20.dp))

    TextField(
        value = password,
        onValueChange = { registerViewModel.onChangePassword(it) },
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
    apellido: String,
    correo: String,
    password: String,
    registerViewModel: RegisterViewModel,
    navigateToLogin: () -> Unit // Callback para redirigir al login
) {
    val success by registerViewModel.success.observeAsState(initial = false)

    Button(
        onClick = {
            val user = CreateUserRequest(username, apellido, correo, password)
            registerViewModel.viewModelScope.launch {
                registerViewModel.onClick(user)
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
        Text("Registrarse")
    }

    // Observa el estado de éxito y redirige si el registro es exitoso
    LaunchedEffect(success) {
        if (success) {
            navigateToLogin()
        }
    }
}

