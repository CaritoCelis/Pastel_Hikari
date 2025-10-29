package com.example.pastel_hikari.datos.repositorio

import com.example.pastel_hikari.datos.base_de_datos.dao.UsuarioDao
import com.example.pastel_hikari.modelo.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * El Repositorio actúa como intermediario entre los ViewModels y las fuentes de datos (DAOs, red, etc).
 * Abstrae el origen de los datos.
 */
class UsuarioRepositorio(private val usuarioDao: UsuarioDao) {

    /**
     * Llama al DAO para registrar un nuevo usuario en la base de datos.
     */
    suspend fun registrar(usuario: Usuario) {
        usuarioDao.registrar(usuario)
    }

    /**
     * Llama al DAO para obtener un usuario por su correo.
     */
    fun obtenerPorCorreo(correo: String): Flow<Usuario?> {
        return usuarioDao.obtenerPorCorreo(correo)
    }
}
