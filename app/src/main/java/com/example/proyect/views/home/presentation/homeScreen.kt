package com.example.proyect.views.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyect.R
import com.example.proyect.views.home.data.model.ProductDTO
import coil.compose.rememberAsyncImagePainter
import com.example.proyect.views.home.data.model.createProductRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToLogin: () -> Unit
) {
    val products by homeViewModel.products.observeAsState(emptyList())
    val loading by homeViewModel.loading.observeAsState(false)
    val error by homeViewModel.error.observeAsState("")
    val success by homeViewModel.success.observeAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(success) {
        if (success) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Producto agregado con éxito")
            }
            homeViewModel.resetSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bienvenido",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = navigateToLogin) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.DarkGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ContainerTop(
                    Modifier.weight(1f),
                    onAddProduct = { product ->
                        homeViewModel.addProduct(product)
                    }
                )
                ContainerBottom(
                    modifier = Modifier.weight(1f),
                    products = products,
                    loading = loading,
                    error = error
                )
            }
        }
    )
}

@Composable
fun ContainerTop(
    modifier: Modifier = Modifier,
    onAddProduct: (createProductRequest) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Disfruta de esta app",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.icon2),
                contentDescription = "icon2",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showDialog = true }) {
                Text(text = "Agregar Producto")
            }
        }
    }

    if (showDialog) {
        AddProductDialog(
            onDismiss = { showDialog = false },
            onAddProduct = { productRequest ->
                onAddProduct(productRequest)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAddProduct: (createProductRequest) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Nuevo Producto") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") }
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("URL de la Imagen") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val productRequest = createProductRequest(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = (precio.toDoubleOrNull() ?: 0.0).toFloat(),
                        imagen = imagen
                    )
                    onAddProduct(productRequest)
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ContainerBottom(
    modifier: Modifier,
    products: List<ProductDTO>,
    loading: Boolean,
    error: String
) {
    Box(
        modifier = modifier.background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        when {
            loading -> {
                // Muestra un indicador de carga
                Text(
                    text = "Cargando productos...",
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
            }
            error.isNotEmpty() -> {
                // Muestra un mensaje de error
                Text(
                    text = "Error: $error",
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
            products.isNotEmpty() -> {
                // Muestra la lista de productos utilizando LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Productos disponibles",
                            textAlign = TextAlign.Start,
                            color = Color.DarkGray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    items(products) { product ->
                        ProductItem(product = product)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
            else -> {
                // Si no hay productos
                Text(
                    text = "No hay productos disponibles.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
fun ProductItem(product: ProductDTO) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.1f))
            .padding(8.dp)
    ) {
        // Imagen del producto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = product.imagen),
                contentDescription = "Imagen del producto",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nombre del producto
        Text(
            text = product.nombre,
            color = Color.DarkGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Descripción del producto
        Text(
            text = product.descripcion,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Precio del producto
        Text(
            text = "Precio: ${product.precio}",
            color = Color.Green,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
