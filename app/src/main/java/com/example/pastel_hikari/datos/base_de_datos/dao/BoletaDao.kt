package com.example.pastel_hikari.datos.base_de_datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.pastel_hikari.modelo.Boleta
import com.example.pastel_hikari.modelo.BoletaConItems
import kotlinx.coroutines.flow.Flow

@Dao
interface BoletaDao {

    // Inserta una boleta y devuelve su ID, que necesitaremos para asociar los items.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarBoleta(boleta: Boleta): Long

    // Obtiene una boleta específica junto con todos sus items asociados.
    @Transaction
    @Query("SELECT * FROM tabla_boletas WHERE id = :boletaId")
    fun obtenerBoletaConItems(boletaId: Long): Flow<BoletaConItems>
}
