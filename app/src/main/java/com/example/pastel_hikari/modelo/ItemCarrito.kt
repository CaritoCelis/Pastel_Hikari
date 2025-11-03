package com.example.pastel_hikari.modelo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tabla_carrito",
    foreignKeys = [
        ForeignKey(
            entity = Boleta::class,
            parentColumns = ["id"],
            childColumns = ["boletaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItemCarrito(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val imagen: String,
    var cantidad: Int,
    // Columna para relacionar el item con una boleta. Es nulo si aún está en el carrito.
    var boletaId: Long? = null 
)
