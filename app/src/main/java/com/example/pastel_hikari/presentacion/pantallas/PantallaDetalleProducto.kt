package com.example.pastel_hikari.presentacion.pantallas

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
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
import com.example.pastel_hikari.presentacion.vista_modelo.CarritoViewModel
import com.example.pastel_hikari.presentacion.vista_modelo.ProductoViewModel
import com.example.pastel_hikari.util.formatCurrency
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    navController: NavController,
    productoId: Int,
    productoViewModel: ProductoViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val producto by productoViewModel.getProducto(productoId).collectAsState(initial = null)
    var cantidad by remember { mutableStateOf(1) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        producto?.let {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "¡Mira este increíble pastel de ${it.nombre} en Pastelería Hikari! Precio: ${formatCurrency(it.precio)}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir producto")
                    }
                }
            )
        }
    ) { paddingValues ->
        producto?.let { prod ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                val resourceId = context.resources.getIdentifier(prod.imagen, "drawable", context.packageName)

                Image(
                    painter = if (resourceId != 0) painterResource(id = resourceId) else painterResource(id = R.drawable.logo),
                    contentDescription = prod.nombre,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(prod.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(formatCurrency(prod.precio), fontSize = 20.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(prod.descripcion, fontSize = 16.sp)
                    Spacer(modifier = Modifier.weight(1f))

                    // Selector de Cantidad
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(onClick = { if (cantidad > 1) cantidad-- }, modifier = Modifier.size(50.dp)) { Text("-") }
                        Text(text = "$cantidad", modifier = Modifier.padding(horizontal = 24.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        OutlinedButton(onClick = { cantidad++ }, modifier = Modifier.size(50.dp)) { Text("+") }
                    }

                    Button(
                        onClick = { 
                            carritoViewModel.agregarOActualizarProducto(prod, cantidad)
                            scope.launch { 
                                snackbarHostState.showSnackbar("$cantidad ${prod.nombre}(s) agregado(s) al carrito") 
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Agregar al Carrito", fontSize = 18.sp)
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
