package com.example.pastel_hikari.presentacion.vista_modelo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel_hikari.datos.AppDatabase
import com.example.pastel_hikari.datos.repositorio.CarritoRepositorio
import com.example.pastel_hikari.modelo.ItemCarrito
import com.example.pastel_hikari.modelo.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: CarritoRepositorio
    
    val items: Flow<List<ItemCarrito>>

    init {
        val carritoDao = AppDatabase.getDatabase(application).carritoDao()
        repositorio = CarritoRepositorio(carritoDao)
        items = repositorio.obtenerItems().map { listaCompleta ->
            listaCompleta.filter { it.boletaId == null }
        }
    }

    fun agregarOActualizarProducto(producto: Producto, cantidad: Int) {
        viewModelScope.launch {
            val itemsActuales = items.first() // Obtenemos la lista ya filtrada de items en el carrito
            val itemExistente = itemsActuales.find { it.productoId == producto.id }

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

    fun eliminarItem(item: ItemCarrito) {
        viewModelScope.launch {
            repositorio.eliminar(item)
        }
    }

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

    fun vaciarCarrito() {
        viewModelScope.launch {
            val itemsEnCarrito = items.first()
            for (item in itemsEnCarrito) {
                repositorio.eliminar(item)
            }
        }
    }
}
