package com.example.pastel_hikari.presentacion.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pastel_hikari.R
import com.example.pastel_hikari.util.SesionManager
import com.example.pastel_hikari.modelo.Producto
import com.example.pastel_hikari.presentacion.vista_modelo.CarritoViewModel
import com.example.pastel_hikari.presentacion.vista_modelo.ProductoViewModel
import com.example.pastel_hikari.util.formatCurrency
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProductos(
    navController: NavController,
    productoViewModel: ProductoViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val context = LocalContext.current
    val sesionManager = remember { SesionManager(context) }
    val nombreUsuario = sesionManager.obtenerNombre() ?: "Usuario"

    val productos by productoViewModel.todosLosProductos.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var mostrarMenuUsuario by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Pastelería Hikari") },
                actions = {
                    // Botón del carrito
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito de Compras")
                    }

                    // Botón del menú de usuario
                    IconButton(onClick = { mostrarMenuUsuario = true }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Menú de usuario")
                    }

                    // Menú desplegable
                    DropdownMenu(
                        expanded = mostrarMenuUsuario,
                        onDismissRequest = { mostrarMenuUsuario = false }
                    ) {
                        // Encabezado con nombre del usuario
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = "Hola, $nombreUsuario",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = sesionManager.obtenerCorreo() ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = { },
                            enabled = false
                        )

                        Divider()

                        // Opción: Mi Perfil
                        DropdownMenuItem(
                            text = { Text("Mi Perfil") },
                            onClick = {
                                mostrarMenuUsuario = false
                                // TODO: Navegar a pantalla de perfil cuando la tengas
                                // navController.navigate("perfil")
                                scope.launch {
                                    snackbarHostState.showSnackbar("Próximamente: Mi Perfil")
                                }
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            }
                        )

                        // Opción: Mis Pedidos
                        DropdownMenuItem(
                            text = { Text("Mis Pedidos") },
                            onClick = {
                                mostrarMenuUsuario = false
                                // TODO: Navegar a pantalla de pedidos cuando la tengas
                                scope.launch {
                                    snackbarHostState.showSnackbar("Próximamente: Mis Pedidos")
                                }
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Receipt, contentDescription = null)
                            }
                        )

                        Divider()

                        // Opción: Cerrar Sesión
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Cerrar Sesión",
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                mostrarMenuUsuario = false
                                sesionManager.cerrarSesion()
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (productos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(productos) { producto ->
                    ProductoCard(
                        producto = producto,
                        onProductoClick = {
                            navController.navigate("producto_detalle/${producto.id}")
                        },
                        onAddToCartClick = {
                            carritoViewModel.agregarOActualizarProducto(producto, 1)
                            scope.launch {
                                snackbarHostState.showSnackbar("¡${producto.nombre} agregado al carrito!")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    onProductoClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onProductoClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            val context = LocalContext.current
            val resourceId = context.resources.getIdentifier(producto.imagen, "drawable", context.packageName)

            Image(
                painter = if (resourceId != 0) painterResource(id = resourceId) else painterResource(id = R.drawable.logo),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = formatCurrency(producto.precio), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    IconButton(onClick = onAddToCartClick) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = "Agregar al carrito")
                    }
                }
            }
        }
    }
}