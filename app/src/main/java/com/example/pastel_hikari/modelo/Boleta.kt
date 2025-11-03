package com.example.pastel_hikari.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tabla_boletas")
data class Boleta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fecha: Date,
    val total: Double,
    val estado: String
)
