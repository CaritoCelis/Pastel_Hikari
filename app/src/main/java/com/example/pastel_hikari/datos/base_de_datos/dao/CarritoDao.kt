package com.example.pastel_hikari.datos.base_de_datos.dao

import androidx.room.*
import com.example.pastel_hikari.modelo.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {

    @Query("SELECT * FROM tabla_carrito")
    fun obtenerTodos(): Flow<List<ItemCarrito>>

    @Query("SELECT * FROM tabla_carrito WHERE productoId = :productoId LIMIT 1")
    suspend fun obtenerPorProductoId(productoId: Int): ItemCarrito?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: ItemCarrito)

    @Update
    suspend fun actualizar(item: ItemCarrito)

    @Delete
    suspend fun eliminar(item: ItemCarrito)

    @Query("DELETE FROM tabla_carrito")
    suspend fun vaciarCarrito()
}
