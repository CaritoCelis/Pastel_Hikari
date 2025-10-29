package com.example.pastel_hikari.presentacion.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pastel_hikari.R
import com.example.pastel_hikari.presentacion.vista_modelo.RegistroViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    navController: NavController,
    registroViewModel: RegistroViewModel = viewModel()
) {
    val uiState by registroViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            navController.navigate("login") { 
                popUpTo("login") { inclusive = true } 
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Crear Cuenta") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- FOTO DE PERFIL ---
            val context = LocalContext.current
            val file = File(context.cacheDir, "temp_image.jpg")
            val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)

            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture(),
                onResult = { success ->
                    if (success) {
                        registroViewModel.onImagenSeleccionada(uri)
                    }
                }
            )

            val galleryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { galleryUri ->
                    registroViewModel.onImagenSeleccionada(galleryUri)
                }
            )

            Image(
                painter = rememberAsyncImagePainter(model = uiState.imagenUri ?: R.drawable.logo),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { cameraLauncher.launch(uri) }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Tomar Foto")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cámara")
                }
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Galería")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galería")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // --- FORMULARIO ---
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = registroViewModel::onNombreChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorNombre != null,
                trailingIcon = { if (uiState.errorNombre != null) Icon(Icons.Default.Error, "Error") }
            )
            uiState.errorNombre?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.apellido,
                onValueChange = registroViewModel::onApellidoChange,
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorApellido != null,
                trailingIcon = { if (uiState.errorApellido != null) Icon(Icons.Default.Error, "Error") }
            )
            uiState.errorApellido?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = uiState.fechaNacimiento,
                onValueChange = registroViewModel::onFechaNacimientoChange,
                label = { Text("Fecha de Nacimiento (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorFechaNacimiento != null,
                trailingIcon = { if (uiState.errorFechaNacimiento != null) Icon(Icons.Default.Error, "Error") }
            )
            uiState.errorFechaNacimiento?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = registroViewModel::onCorreoChange,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorCorreo != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                trailingIcon = { if (uiState.errorCorreo != null) Icon(Icons.Default.Error, "Error") }
            )
            uiState.errorCorreo?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.contrasena,
                onValueChange = registroViewModel::onContrasenaChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorContrasena != null,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = { if (uiState.errorContrasena != null) Icon(Icons.Default.Error, "Error") }
            )
            uiState.errorContrasena?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = registroViewModel::registrarUsuario,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Registrarse")
            }
        }
    }
}
