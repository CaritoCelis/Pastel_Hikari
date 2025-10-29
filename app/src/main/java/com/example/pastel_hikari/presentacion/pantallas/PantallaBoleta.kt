package com.example.pastel_hikari.presentacion.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pastel_hikari.presentacion.vista_modelo.BoletaViewModel
import com.example.pastel_hikari.util.formatCurrency
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBoleta(
    navController: NavController,
    boletaId: Long,
    boletaViewModel: BoletaViewModel = viewModel()
) {
    val boletaConItems by boletaViewModel.obtenerBoleta(boletaId).collectAsState(initial = null)

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Column {
                        Text("Pastelería Hikari")
                        Text("Detalle de la Compra", fontSize = 12.sp, fontWeight = FontWeight.Normal)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al inicio")
                    }
                }
            )
        }
    ) { paddingValues ->
        boletaConItems?.let { detalles ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Encabezado de la boleta
                item {
                    Text("Boleta N°: ${detalles.boleta.id}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    Text("Fecha: ${formatoFecha.format(detalles.boleta.fecha)}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Estado: ${detalles.boleta.estado}", fontWeight = FontWeight.SemiBold)
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text("Resumen de la compra:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Lista de items
                items(detalles.items) { item ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("${item.cantidad}x ${item.nombre}", modifier = Modifier.weight(1f))
                        Text(formatCurrency(item.precio * item.cantidad))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Pie de la boleta
                item {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text("Total: ", fontSize = 20.sp, fontWeight = FontWeight.Normal)
                        Text(formatCurrency(detalles.boleta.total), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
