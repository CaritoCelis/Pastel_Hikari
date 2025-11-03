package com.example.pastel_hikari.presentacion.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pastel_hikari.R
import com.example.pastel_hikari.presentacion.vista_modelo.RegistroViewModel
import kotlinx.coroutines.delay
import java.io.File
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    navController: NavController,
    registroViewModel: RegistroViewModel = viewModel()
) {
    val uiState by registroViewModel.uiState.collectAsState()
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarConfirmarContrasena by remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }

    var presionado by remember { mutableStateOf(false) }
    val escalaBoton by animateFloatAsState(
        targetValue = if (presionado) 0.9f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "escalaBoton"
    )
    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = registroViewModel::onNombreChange,
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.errorNombre != null,
                    trailingIcon = { if (uiState.errorNombre != null) Icon(Icons.Default.Error, "Error") }
                )
                uiState.errorNombre?.let { error ->
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
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
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
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
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
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
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.contrasena,
                    onValueChange = registroViewModel::onContrasenaChange,
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.errorContrasena != null,
                    visualTransformation = if (mostrarContrasena)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                            Icon(
                                imageVector = if (mostrarContrasena)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (mostrarContrasena)
                                    "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    }
                )
                uiState.errorContrasena?.let { error ->
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                if (uiState.contrasena.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    val fuerza = calcularFuerzaContrasena(uiState.contrasena)
                    LinearProgressIndicator(
                        progress = fuerza / 4f,
                        modifier = Modifier.fillMaxWidth(),
                        color = when (fuerza) {
                            1 -> MaterialTheme.colorScheme.error
                            2 -> Color(0xFFFF9800)
                            3 -> Color(0xFFFFC107)
                            4 -> Color(0xFF4CAF50)
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                    Text(
                        text = when (fuerza) {
                            1 -> "Contraseña débil"
                            2 -> "Contraseña media"
                            3 -> "Contraseña fuerte"
                            4 -> "Contraseña muy fuerte"
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when (fuerza) {
                            1 -> MaterialTheme.colorScheme.error
                            2 -> Color(0xFFFF9800)
                            3 -> Color(0xFFFFC107)
                            4 -> Color(0xFF4CAF50)
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.confirmarContrasena,
                    onValueChange = registroViewModel::onConfirmarContrasenaChange,
                    label = { Text("Confirmar Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.errorConfirmarContrasena != null,
                    visualTransformation = if (mostrarConfirmarContrasena)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { mostrarConfirmarContrasena = !mostrarConfirmarContrasena }) {
                            Icon(
                                imageVector = if (mostrarConfirmarContrasena)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (mostrarConfirmarContrasena)
                                    "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    }
                )
                uiState.errorConfirmarContrasena?.let { error ->
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.aceptaTerminos,
                        onCheckedChange = registroViewModel::onAceptaTerminosChange
                    )
                    Text("Acepto los ", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "Términos y Condiciones",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { }
                    )
                }

                uiState.errorTerminos?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {
                        presionado = true
                        registroViewModel.registrarUsuario()
                        // 🔹 Usamos corrutina en lugar de LaunchedEffect
                        scope.launch {
                            delay(150)
                            presionado = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .scale(escalaBoton),
                    enabled = !uiState.estaCargando
                ) {
                    if (uiState.estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Registrarse")
                    }
                }

                uiState.mensajeError?.let { mensaje ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = mensaje,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

private fun calcularFuerzaContrasena(contrasena: String): Int {
    var fuerza = 0
    if (contrasena.length >= 8) fuerza++
    if (contrasena.any { it.isUpperCase() }) fuerza++
    if (contrasena.any { it.isLowerCase() }) fuerza++
    if (contrasena.any { it.isDigit() }) fuerza++
    if (contrasena.any { !it.isLetterOrDigit() }) fuerza++
    return when {
        fuerza <= 2 -> 1
        fuerza == 3 -> 2
        fuerza == 4 -> 3
        else -> 4
    }
}
