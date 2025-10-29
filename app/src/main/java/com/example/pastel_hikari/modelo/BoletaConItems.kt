package com.example.pastel_hikari.modelo

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Esta clase representa la relación uno-a-muchos entre una Boleta y sus Items.
 */
data class BoletaConItems(
    @Embedded
    val boleta: Boleta,

    @Relation(
        parentColumn = "id", // Columna de la tabla Boleta (la tabla padre)
        entityColumn = "boletaId" // Columna de la tabla ItemCarrito (la tabla hija)
    )
    val items: List<ItemCarrito>
)
