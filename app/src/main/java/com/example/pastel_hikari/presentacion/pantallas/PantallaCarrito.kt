package com.example.pastel_hikari.presentacion.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.pastel_hikari.modelo.ItemCarrito
import com.example.pastel_hikari.presentacion.vista_modelo.BoletaViewModel
import com.example.pastel_hikari.presentacion.vista_modelo.CarritoViewModel
import com.example.pastel_hikari.util.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(
    navController: NavController,
    carritoViewModel: CarritoViewModel = viewModel(),
    boletaViewModel: BoletaViewModel = viewModel()
) {
    // 'items' ahora viene filtrado directamente desde el ViewModel
    val items by carritoViewModel.items.collectAsState(initial = emptyList())
    val precioTotal = items.sumOf { it.precio * it.cantidad }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Mi Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("El carrito está vacío", fontSize = 18.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items) { item ->
                        ItemCarritoCard(
                            item = item,
                            onEliminar = { carritoViewModel.eliminarItem(item) },
                            onCambiarCantidad = { nuevaCantidad -> carritoViewModel.cambiarCantidad(item, nuevaCantidad) }
                        )
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Total: ${formatCurrency(precioTotal)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.End)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            boletaViewModel.crearBoletaYAsignarItems { boletaId ->
                                navController.navigate("boleta/${boletaId}") {
                                    popUpTo("home") 
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Proceder al Pago", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCarritoCard(
    item: ItemCarrito,
    onEliminar: () -> Unit,
    onCambiarCantidad: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        val resourceId = context.resources.getIdentifier(item.imagen, "drawable", context.packageName)

        Image(
            painter = if (resourceId != 0) painterResource(id = resourceId) else painterResource(id = R.drawable.logo),
            contentDescription = item.nombre,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(item.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 2)
            Text(formatCurrency(item.precio), fontSize = 14.sp)
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onCambiarCantidad(item.cantidad - 1) }) {
                Icon(Icons.Default.Remove, contentDescription = "Disminuir cantidad")
            }
            Text("${item.cantidad}", fontWeight = FontWeight.Bold)
            IconButton(onClick = { onCambiarCantidad(item.cantidad + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Aumentar cantidad")
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar item")
            }
        }
    }
}
