package com.example.pastel_hikari.presentacion.vista_modelo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel_hikari.datos.AppDatabase
import com.example.pastel_hikari.datos.base_de_datos.dao.CarritoDao
import com.example.pastel_hikari.modelo.ItemCarrito
import com.example.pastel_hikari.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class EstadoAgregar {
    INICIAL,
    AGREGADO
}

data class DetalleProductoUiState(
    val estado: EstadoAgregar = EstadoAgregar.INICIAL
)

class DetalleProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val carritoDao: CarritoDao

    private val _uiState = MutableStateFlow(DetalleProductoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carritoDao = AppDatabase.getDatabase(application).carritoDao()
    }

    fun agregarAlCarrito(producto: Producto) {
        viewModelScope.launch {
            val itemExistente = carritoDao.obtenerPorProductoId(producto.id)

            if (itemExistente != null) {
                val itemActualizado = itemExistente.copy(cantidad = itemExistente.cantidad + 1)
                carritoDao.actualizar(itemActualizado)
            } else {
                val nuevoItem = ItemCarrito(
                    productoId = producto.id,
                    nombre = producto.nombre,
                    precio = producto.precio,
                    imagen = producto.imagen,
                    cantidad = 1
                )
                carritoDao.insertar(nuevoItem)
            }
            _uiState.update { it.copy(estado = EstadoAgregar.AGREGADO) }
        }
    }

    fun resetEstado() {
        _uiState.update { it.copy(estado = EstadoAgregar.INICIAL) }
    }
}
