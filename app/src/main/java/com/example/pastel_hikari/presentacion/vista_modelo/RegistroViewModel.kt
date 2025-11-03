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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val fechaNacimiento: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val imagenUri: Uri? = null,
    val aceptaTerminos: Boolean = false,
    val errorNombre: String? = null,
    val errorApellido: String? = null,
    val errorFechaNacimiento: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmarContrasena: String? = null,
    val errorTerminos: String? = null,
    val mensajeError: String? = null,
    val estaCargando: Boolean = false,
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
        _uiState.update { it.copy(
            contrasena = contrasena,
            errorContrasena = null,
            errorConfirmarContrasena = null // Limpia también el error de confirmación
        )}
    }

    fun onConfirmarContrasenaChange(confirmarContrasena: String) {
        _uiState.update { it.copy(
            confirmarContrasena = confirmarContrasena,
            errorConfirmarContrasena = null
        )}
    }

    fun onImagenSeleccionada(uri: Uri?) {
        _uiState.update { it.copy(imagenUri = uri) }
    }

    fun onAceptaTerminosChange(acepta: Boolean) {
        _uiState.update { it.copy(
            aceptaTerminos = acepta,
            errorTerminos = null
        )}
    }

    fun registrarUsuario() {
        val state = _uiState.value

        // Validaciones
        val errorNombre = when {
            state.nombre.isBlank() -> "El nombre no puede estar vacío"
            state.nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            else -> null
        }

        val errorApellido = when {
            state.apellido.isBlank() -> "El apellido no puede estar vacío"
            state.apellido.length < 2 -> "El apellido debe tener al menos 2 caracteres"
            else -> null
        }

        val errorFecha = when {
            state.fechaNacimiento.isBlank() -> "La fecha no puede estar vacía"
            !validarFormatoFecha(state.fechaNacimiento) -> "Formato inválido. Use DD/MM/AAAA"
            else -> null
        }

        val errorCorreo = when {
            state.correo.isBlank() -> "El correo no puede estar vacío"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(state.correo).matches() -> "El correo no es válido"
            else -> null
        }

        val errorContra = when {
            state.contrasena.isBlank() -> "La contraseña no puede estar vacía"
            state.contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }

        val errorConfirmarContra = when {
            state.confirmarContrasena.isBlank() -> "Debe confirmar la contraseña"
            state.contrasena != state.confirmarContrasena -> "Las contraseñas no coinciden"
            else -> null
        }

        val errorTerminos = if (!state.aceptaTerminos) {
            "Debe aceptar los términos y condiciones"
        } else null

        if (errorNombre != null || errorApellido != null || errorFecha != null ||
            errorCorreo != null || errorContra != null || errorConfirmarContra != null ||
            errorTerminos != null) {
            _uiState.update { it.copy(
                errorNombre = errorNombre,
                errorApellido = errorApellido,
                errorFechaNacimiento = errorFecha,
                errorCorreo = errorCorreo,
                errorContrasena = errorContra,
                errorConfirmarContrasena = errorConfirmarContra,
                errorTerminos = errorTerminos
            )}
            return
        }

        _uiState.update { it.copy(estaCargando = true, mensajeError = null) }

        viewModelScope.launch {
            try {
                // Verificar si el correo ya existe
                val usuarioExistente = repositorio.obtenerPorCorreo(state.correo).first()

                if (usuarioExistente != null) {
                    _uiState.update { it.copy(
                        estaCargando = false,
                        errorCorreo = "Este correo ya está registrado"
                    )}
                    return@launch
                }

                // Crear y registrar el nuevo usuario
                val nuevoUsuario = Usuario(
                    nombre = state.nombre,
                    apellido = state.apellido,
                    fechaNacimiento = state.fechaNacimiento,
                    correo = state.correo,
                    contrasena = state.contrasena,
                    imagenPerfilUri = state.imagenUri?.toString()
                )

                repositorio.registrar(nuevoUsuario)

                _uiState.update { it.copy(
                    estaCargando = false,
                    registroExitoso = true
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    estaCargando = false,
                    mensajeError = "Error al registrar: ${e.message}"
                )}
            }
        }
    }

    private fun validarFormatoFecha(fecha: String): Boolean {
        val fechaRegex = "^\\d{2}/\\d{2}/\\d{4}$".toRegex()
        if (!fechaRegex.matches(fecha)) return false

        val partes = fecha.split("/")
        val dia = partes[0].toIntOrNull() ?: return false
        val mes = partes[1].toIntOrNull() ?: return false
        val anio = partes[2].toIntOrNull() ?: return false

        // Validar rangos básicos
        if (dia !in 1..31 || mes !in 1..12 || anio < 1900 || anio > 2024) return false

        return true
    }
}