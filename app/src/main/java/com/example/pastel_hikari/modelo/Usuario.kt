package com.example.pastel_hikari.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val correo: String,
    val contrasena: String,
    // Añadiremos la foto de perfil más adelante
    val imagenPerfilUri: String? = null 
)
