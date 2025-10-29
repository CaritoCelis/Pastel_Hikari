package com.example.pastel_hikari.datos.repositorio

import com.example.pastel_hikari.datos.base_de_datos.dao.ProductoDao
import com.example.pastel_hikari.modelo.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepositorio(private val productoDao: ProductoDao) {

    val allProductos: Flow<List<Producto>> = productoDao.getProductos()

    fun getProducto(id: Int): Flow<Producto> {
        return productoDao.getProducto(id)
    }

    suspend fun insertAll(productos: List<Producto>) {
        productoDao.insertAll(productos)
    }
}
