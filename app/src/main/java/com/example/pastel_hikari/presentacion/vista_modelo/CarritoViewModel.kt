package com.example.pastel_hikari.presentacion.vista_modelo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel_hikari.datos.AppDatabase
import com.example.pastel_hikari.datos.repositorio.CarritoRepositorio
import com.example.pastel_hikari.modelo.ItemCarrito
import com.example.pastel_hikari.modelo.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: CarritoRepositorio
    
    val items: Flow<List<ItemCarrito>>

    init {
        val carritoDao = AppDatabase.getDatabase(application).carritoDao()
        repositorio = CarritoRepositorio(carritoDao)
        items = repositorio.obtenerItems()
    }

    /**
     * Agrega un producto al carrito. Si ya existe, actualiza la cantidad.
     */
    fun agregarOActualizarProducto(producto: Producto, cantidad: Int) {
        viewModelScope.launch {
            val itemExistente = repositorio.obtenerPorProductoId(producto.id)

            if (itemExistente != null) {
                val itemActualizado = itemExistente.copy(cantidad = itemExistente.cantidad + cantidad)
                repositorio.actualizar(itemActualizado)
            } else {
                val nuevoItem = ItemCarrito(
                    productoId = producto.id,
                    nombre = producto.nombre,
                    precio = producto.precio,
                    imagen = producto.imagen,
                    cantidad = cantidad
                )
                repositorio.insertar(nuevoItem)
            }
        }
    }

    /**
     * Elimina un item específico del carrito.
     */
    fun eliminarItem(item: ItemCarrito) {
        viewModelScope.launch {
            repositorio.eliminar(item)
        }
    }

    /**
     * Cambia la cantidad de un item específico en el carrito.
     * Si la cantidad llega a 0, se elimina.
     */
    fun cambiarCantidad(item: ItemCarrito, nuevaCantidad: Int) {
        viewModelScope.launch {
            if (nuevaCantidad > 0) {
                val itemActualizado = item.copy(cantidad = nuevaCantidad)
                repositorio.actualizar(itemActualizado)
            } else {
                repositorio.eliminar(item)
            }
        }
    }

    /**
     * Vacía todo el carrito de compras.
     */
    fun vaciarCarrito() {
        viewModelScope.launch {
            repositorio.vaciarCarrito()
        }
    }
}
