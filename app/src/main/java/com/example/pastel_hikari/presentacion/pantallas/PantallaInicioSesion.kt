package com.example.pastel_hikari.presentacion.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pastel_hikari.R
import com.example.pastel_hikari.presentacion.vista_modelo.EstadoLogin
import com.example.pastel_hikari.presentacion.vista_modelo.InicioSesionViewModel
@Composable
fun PantallaInicioSesion(
    navController: NavController,
    inicioSesionViewModel: InicioSesionViewModel = viewModel()
) {
    val uiState by inicioSesionViewModel.uiState.collectAsState()

    // Snackbar para mostrar mensajes de error
    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }

    // Efecto para manejar los cambios de estado del login
    LaunchedEffect(uiState.estado) {
        when (uiState.estado) {
            EstadoLogin.EXITOSO -> {
                // Navega a la pantalla principal y limpia el historial para no volver aquí.
                navController.navigate("home") { // Asumimos que "home" será la ruta principal
                    popUpTo("login") { inclusive = true }
                }
            }
            EstadoLogin.ERROR -> {
                snackbarHostState.showSnackbar("Correo o contraseña incorrectos")
                inicioSesionViewModel.resetEstado() // Reseteamos el estado para poder intentarlo de nuevo
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo y Banner
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo de la pastelería")
            Spacer(modifier = Modifier.height(16.dp))
            Image(painter = painterResource(id = R.drawable.baner), contentDescription = "Banner de la pastelería")
            Spacer(modifier = Modifier.height(32.dp))

            // --- CAMPO CORREO ---
            OutlinedTextField(
                value = uiState.correo,
                onValueChange = inicioSesionViewModel::onCorreoChange,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.estado == EstadoLogin.ERROR,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- CAMPO CONTRASEÑA ---
            OutlinedTextField(
                value = uiState.contrasena,
                onValueChange = inicioSesionViewModel::onContrasenaChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.estado == EstadoLogin.ERROR,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = inicioSesionViewModel::iniciarSesion,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = uiState.estado != EstadoLogin.CARGANDO // Deshabilitado mientras carga
            ) {
                if (uiState.estado == EstadoLogin.CARGANDO) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Iniciar Sesión")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("registro") }) { // Asumimos que "registro" es la ruta
                Text("¿No tienes una cuenta? Regístrate")
            }
        }
    }
}
