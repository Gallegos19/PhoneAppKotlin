package com.example.proyect.views.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.proyect.views.home.data.model.ProductDTO
import com.example.proyect.views.home.data.model.createProductRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToLogin: () -> Unit,
    navigateToMap: () -> Unit
) {
    val products = homeViewModel.products
    val loading = homeViewModel.loading
    val error = homeViewModel.error

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val deleteSuccess = homeViewModel.deleteSuccess
    val success = homeViewModel.success

    // Observar cambios en deleteSuccess
    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Producto eliminado con éxito")
            }
            homeViewModel.resetDeleteSuccess()
            homeViewModel.fetchProducts()
        }
    }

    // Observar cambios en success
    LaunchedEffect(success) {
        if (success) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Producto agregado con éxito")
            }
            homeViewModel.resetSuccess()
            homeViewModel.fetchProducts()
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
                    IconButton(onClick = { homeViewModel.fetchProducts() }) {
                        Icon(
                            imageVector = Icons.Default.Autorenew,
                            contentDescription = "Recargar productos",
                            tint = Color.DarkGray
                        )
                    }
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
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = navigateToMap,
                    containerColor = Color.Blue,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = "Mapa", tint = Color.White)
                }
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = Color.Green
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Producto", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Productos disponibles",
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(products) { product ->
                ProductItem(product = product, onDeleteProduct = { id ->
                    homeViewModel.deleteProduct(id)
                },
                    onReload ={homeViewModel.fetchProducts()}
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    if (showDialog) {
        AddProductDialog(
            onDismiss = { showDialog = false },
            onAddProduct = { productRequest ->
                homeViewModel.addProduct(productRequest)
                showDialog = false
            }
        )
    }
}


@Composable
fun ProductItem(product: ProductDTO, onDeleteProduct: (Int) -> Unit,  onReload: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.1f))
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = product.imagen),
            contentDescription = "Imagen del producto",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.nombre, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = product.descripcion, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Precio: ${product.precio}", color = Color.Green, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))

        DeleteButton(
            onClick = { onDeleteProduct(product.idproduct) },
            onReload = { onReload }
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
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = imagen, onValueChange = { imagen = it }, label = { Text("URL de la Imagen") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val productRequest = createProductRequest(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = (precio.toDoubleOrNull() ?: 0.0).toFloat(),
                    imagen = imagen
                )
                onAddProduct(productRequest)
            }) {
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
fun DeleteButton(onClick: () -> Unit, onReload: () -> Unit) {
    IconButton(
        onClick = {
            onClick()  // Primero se elimina el producto
            onReload() // Luego se actualiza la lista de productos
        },
        modifier = Modifier
            .size(40.dp) // Ajusta el tamaño del botón
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = MaterialTheme.colorScheme.error // Color rojo
        )
    }
}


