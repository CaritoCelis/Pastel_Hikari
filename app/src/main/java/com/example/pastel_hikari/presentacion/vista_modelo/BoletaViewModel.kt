package com.example.pastel_hikari.presentacion.vista_modelo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel_hikari.datos.AppDatabase
import com.example.pastel_hikari.modelo.Boleta
import com.example.pastel_hikari.modelo.BoletaConItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first // ¡EL IMPORT CLAVE QUE FALTABA!
import kotlinx.coroutines.launch
import java.util.Date

class BoletaViewModel(application: Application) : AndroidViewModel(application) {

    private val boletaDao = AppDatabase.getDatabase(application).boletaDao()
    private val carritoDao = AppDatabase.getDatabase(application).carritoDao()

    fun crearBoletaYAsignarItems(onBoletaCreada: (Long) -> Unit) {
        viewModelScope.launch {
            val todosLosItems = carritoDao.obtenerTodos().first()

            val itemsEnCarrito = todosLosItems.filter { it.boletaId == null }

            if (itemsEnCarrito.isNotEmpty()) {
                val total = itemsEnCarrito.sumOf { it.precio * it.cantidad }

                val nuevaBoleta = Boleta(fecha = Date(), total = total, estado = "En preparación")

                val boletaId = boletaDao.insertarBoleta(nuevaBoleta)

                for (item in itemsEnCarrito) {
                    val itemActualizado = item.copy(boletaId = boletaId)
                    carritoDao.actualizar(itemActualizado)
                }

                onBoletaCreada(boletaId)
            }
        }
    }

    fun obtenerBoleta(boletaId: Long): Flow<BoletaConItems> {
        return boletaDao.obtenerBoletaConItems(boletaId)
    }
}
