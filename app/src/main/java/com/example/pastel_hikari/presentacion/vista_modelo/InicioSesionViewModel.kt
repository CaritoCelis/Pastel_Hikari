package com.example.pastel_hikari.presentacion.vista_modelo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel_hikari.datos.AppDatabase
import com.example.pastel_hikari.util.SesionManager
import com.example.pastel_hikari.datos.repositorio.UsuarioRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
enum class EstadoLogin {
    INICIAL,
    CARGANDO,
    EXITOSO,
    ERROR
}

data class InicioSesionUiState(
    val correo: String = "",
    val contrasena: String = "",
    val estado: EstadoLogin = EstadoLogin.INICIAL
)

class InicioSesionViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: UsuarioRepositorio
    private val sesionManager: SesionManager

    private val _uiState = MutableStateFlow(InicioSesionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
        repositorio = UsuarioRepositorio(usuarioDao)
        sesionManager = SesionManager(application)
    }

    fun onCorreoChange(correo: String) {
        _uiState.update { it.copy(correo = correo) }
    }

    fun onContrasenaChange(contrasena: String) {
        _uiState.update { it.copy(contrasena = contrasena) }
    }

    fun iniciarSesion() {
        // No continuar si ya está cargando
        if (_uiState.value.estado == EstadoLogin.CARGANDO) return

        // Actualizamos el estado a CARGANDO para que la UI pueda mostrar un spinner, por ejemplo.
        _uiState.update { it.copy(estado = EstadoLogin.CARGANDO) }

        viewModelScope.launch {
            val state = _uiState.value
            val usuario = repositorio.obtenerPorCorreo(state.correo).firstOrNull()

            if (usuario != null && usuario.contrasena == state.contrasena) {
                // ¡Éxito! Guardar la sesión
                sesionManager.guardarSesion(
                    usuarioId = usuario.id,
                    correo = usuario.correo,
                    nombre = usuario.nombre
                )

                _uiState.update { it.copy(estado = EstadoLogin.EXITOSO) }
            } else {
                // Error: El usuario no existe o la contraseña es incorrecta.
                _uiState.update { it.copy(estado = EstadoLogin.ERROR) }
            }
        }
    }

    // Función para resetear el estado de error y que el mensaje desaparezca
    fun resetEstado() {
        _uiState.update { it.copy(estado = EstadoLogin.INICIAL) }
    }
}