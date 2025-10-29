package com.example.pastel_hikari.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_carrito")
data class ItemCarrito(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int, // Para saber a qué producto se refiere
    val nombre: String,
    val precio: Double,
    val imagen: String,
    var cantidad: Int
)
