package com.example.pastel_hikari.util
import android.content.Context
import android.content.SharedPreferences

class SesionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USUARIO_ID = "usuario_id"
        private const val KEY_CORREO = "correo"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_ESTA_LOGUEADO = "esta_logueado"
    }

    // Guardar sesión cuando el usuario hace login
    fun guardarSesion(usuarioId: Int, correo: String, nombre: String) {
        sharedPreferences.edit().apply {
            putInt(KEY_USUARIO_ID, usuarioId)
            putString(KEY_CORREO, correo)
            putString(KEY_NOMBRE, nombre)
            putBoolean(KEY_ESTA_LOGUEADO, true)
            apply()
        }
    }

    // Obtener el ID del usuario logueado
    fun obtenerUsuarioId(): Int {
        return sharedPreferences.getInt(KEY_USUARIO_ID, -1)
    }

    // Obtener el correo del usuario logueado
    fun obtenerCorreo(): String? {
        return sharedPreferences.getString(KEY_CORREO, null)
    }

    // Obtener el nombre del usuario logueado
    fun obtenerNombre(): String? {
        return sharedPreferences.getString(KEY_NOMBRE, null)
    }

    // Verificar si hay un usuario logueado
    fun estaLogueado(): Boolean {
        return sharedPreferences.getBoolean(KEY_ESTA_LOGUEADO, false)
    }

    // Cerrar sesión (borrar todos los datos)
    fun cerrarSesion() {
        sharedPreferences.edit().clear().apply()
    }
}