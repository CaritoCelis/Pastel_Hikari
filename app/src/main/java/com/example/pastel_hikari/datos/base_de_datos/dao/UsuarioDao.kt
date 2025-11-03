package com.example.pastel_hikari.datos.base_de_datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pastel_hikari.modelo.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * Si ya existe un usuario con el mismo id, no lo reemplaza.
     */
    @Insert
    suspend fun registrar(usuario: Usuario)

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * Devuelve un Flow, que emitirá el usuario si se encuentra, o null si no.
     * El Flow permite que la UI reaccione a cambios en los datos del usuario.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    fun obtenerPorCorreo(correo: String): Flow<Usuario?>

}
