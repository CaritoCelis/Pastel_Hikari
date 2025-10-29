package com.example.pastel_hikari.presentacion.vista_modelo

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel_hikari.datos.AppDatabase
import com.example.pastel_hikari.datos.repositorio.UsuarioRepositorio
import com.example.pastel_hikari.modelo.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val fechaNacimiento: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val imagenUri: Uri? = null, // Campo para la imagen
    val errorNombre: String? = null,
    val errorApellido: String? = null,
    val errorFechaNacimiento: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val registroExitoso: Boolean = false
)

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: UsuarioRepositorio

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
        repositorio = UsuarioRepositorio(usuarioDao)
    }

    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, errorNombre = null) }
    }

    fun onApellidoChange(apellido: String) {
        _uiState.update { it.copy(apellido = apellido, errorApellido = null) }
    }

    fun onFechaNacimientoChange(fecha: String) {
        _uiState.update { it.copy(fechaNacimiento = fecha, errorFechaNacimiento = null) }
    }

    fun onCorreoChange(correo: String) {
        _uiState.update { it.copy(correo = correo, errorCorreo = null) }
    }

    fun onContrasenaChange(contrasena: String) {
        _uiState.update { it.copy(contrasena = contrasena, errorContrasena = null) }
    }

    // Nueva función para manejar la selección de la imagen
    fun onImagenSeleccionada(uri: Uri?) {
        _uiState.update { it.copy(imagenUri = uri) }
    }

    fun registrarUsuario() {
        val state = _uiState.value

        val errorNombre = if (state.nombre.isBlank()) "El nombre no puede estar vacío" else null
        val errorApellido = if (state.apellido.isBlank()) "El apellido no puede estar vacío" else null
        val errorFecha = if (state.fechaNacimiento.isBlank()) "La fecha no puede estar vacía" else null
        val errorCorreo = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.correo).matches()) "El correo no es válido" else null
        val errorContra = if (state.contrasena.length < 6) "La contraseña debe tener al menos 6 caracteres" else null

        if (errorNombre != null || errorApellido != null || errorFecha != null || errorCorreo != null || errorContra != null) {
            _uiState.update { it.copy(
                errorNombre = errorNombre,
                errorApellido = errorApellido,
                errorFechaNacimiento = errorFecha,
                errorCorreo = errorCorreo,
                errorContrasena = errorContra
            )}
            return
        }

        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = state.nombre,
                apellido = state.apellido,
                fechaNacimiento = state.fechaNacimiento,
                correo = state.correo,
                contrasena = state.contrasena,
                imagenPerfilUri = state.imagenUri?.toString() // Guardamos la URI como String
            )
            repositorio.registrar(nuevoUsuario)
            _uiState.update { it.copy(registroExitoso = true) }
        }
    }
}
