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
            // 1. Obtenemos la lista actual de TODOS los items de la base de datos.
            // .first() es una función suspend que espera el primer valor del Flow y lo devuelve como una lista.
            val todosLosItems = carritoDao.obtenerTodos().first()

            // 2. Filtramos la lista para quedarnos solo con los que están en el carrito (no tienen boletaId).
            val itemsEnCarrito = todosLosItems.filter { it.boletaId == null }

            if (itemsEnCarrito.isNotEmpty()) {
                // 3. Calculamos el total a partir de la lista filtrada.
                val total = itemsEnCarrito.sumOf { it.precio * it.cantidad }

                // 4. Creamos la boleta.
                val nuevaBoleta = Boleta(fecha = Date(), total = total, estado = "En preparación")

                // 5. Insertamos la boleta y obtenemos su nuevo ID.
                val boletaId = boletaDao.insertarBoleta(nuevaBoleta)

                // 6. Actualizamos CADA item del carrito con el nuevo ID de la boleta.
                for (item in itemsEnCarrito) {
                    val itemActualizado = item.copy(boletaId = boletaId)
                    carritoDao.actualizar(itemActualizado)
                }

                // 7. Informamos a la UI que la boleta fue creada para que pueda navegar.
                onBoletaCreada(boletaId)
            }
        }
    }

    fun obtenerBoleta(boletaId: Long): Flow<BoletaConItems> {
        return boletaDao.obtenerBoletaConItems(boletaId)
    }
}
