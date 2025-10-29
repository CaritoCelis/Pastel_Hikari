package com.example.pastel_hikari.datos.base_de_datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pastel_hikari.modelo.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos")
    fun getProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun getProducto(id: Int): Flow<Producto>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(productos: List<Producto>)

}
