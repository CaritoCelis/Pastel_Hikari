package com.example.pastel_hikari.datos.repositorio

import com.example.pastel_hikari.datos.base_de_datos.dao.CarritoDao
import com.example.pastel_hikari.modelo.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CarritoRepositorio(private val carritoDao: CarritoDao) {

    fun obtenerItems(): Flow<List<ItemCarrito>> {
        return carritoDao.obtenerTodos()
    }

    suspend fun obtenerPorProductoId(productoId: Int): ItemCarrito? {
        return carritoDao.obtenerPorProductoId(productoId)
    }

    suspend fun insertar(item: ItemCarrito) {
        carritoDao.insertar(item)
    }

    suspend fun actualizar(item: ItemCarrito) {
        carritoDao.actualizar(item)
    }

    suspend fun eliminar(item: ItemCarrito) {
        carritoDao.eliminar(item)
    }

    suspend fun vaciarCarrito() {
        carritoDao.vaciarCarrito()
    }
}
