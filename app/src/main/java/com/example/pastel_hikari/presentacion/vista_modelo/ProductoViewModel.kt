package com.example.pastel_hikari.presentacion.vista_modelo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel_hikari.datos.AppDatabase
import com.example.pastel_hikari.datos.repositorio.ProductoRepositorio
import com.example.pastel_hikari.modelo.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: ProductoRepositorio

    // Expone un Flow con la lista de productos para que la UI la observe.
    val todosLosProductos: Flow<List<Producto>>

    init {
        val productoDao = AppDatabase.getDatabase(application).productoDao()
        repositorio = ProductoRepositorio(productoDao)
        todosLosProductos = repositorio.allProductos

        // Llama a la función para poblar la base de datos si está vacía.
        insertarProductosIniciales()
    }

    /**
     * Inserta una lista de productos de ejemplo en la base de datos.
     * Se ejecuta en una corrutina para no bloquear el hilo principal.
     */
    private fun insertarProductosIniciales() {
        viewModelScope.launch(Dispatchers.IO) { // Usamos Dispatchers.IO para operaciones de base de datos
            val productosDeEjemplo = listOf(
                Producto(nombre = "Cheesecake de Oreo", descripcion = "Cremoso cheesecake con base de galletas Oreo y trozos de Oreo.", precio = 25000.0, imagen = "cheesecake_oreo"),
                Producto(nombre = "Mini Cheesecakes de Limón", descripcion = "Caja de 7 unidades de refrescantes mini cheesecakes de limón.", precio = 18000.0, imagen = "minicheesecakes_limon_7unidades"),
                Producto(nombre = "Paletas Corazón", descripcion = "Caja de 3 unidades de paletas de pastel en forma de corazón.", precio = 9000.0, imagen = "paletas_corazon3unidades"),
                Producto(nombre = "Paletas Surtidas", descripcion = "Caja de 7 unidades de paletas de pastel de diferentes sabores.", precio = 15000.0, imagen = "paletas_7unidades"),
                Producto(nombre = "Paletas Blancas y Rosadas", descripcion = "Caja de 4 unidades de elegantes paletas de pastel blancas y rosadas.", precio = 12000.0, imagen = "paletas_blancoyrosado_4unidades"),
                Producto(nombre = "Tartaleta de Frutas", descripcion = "Deliciosa tartaleta con base de masa quebrada y frutas de la estación.", precio = 20000.0, imagen = "tartaleta_frutas"),
                Producto(nombre = "Tartaleta de Manzana", descripcion = "Clásica tartaleta de manzana con un toque de canela.", precio = 18000.0, imagen = "tartaleta_manzana"),
                Producto(nombre = "Cupcakes Rosados (12u)", descripcion = "Caja de 12 unidades de tiernos cupcakes con frosting rosado.", precio = 22000.0, imagen = "cupcakesrosa12unidades"),
                Producto(nombre = "Galletas con Chocolate (12u)", descripcion = "Caja de 12 unidades de galletas con chips de chocolate.", precio = 10000.0, imagen = "galletas_conchocolate_12unidades"),
                Producto(nombre = "Donuts con Chispas (6u)", descripcion = "Caja de 6 unidades de donuts cubiertas con glaseado y chispas de colores.", precio = 12000.0, imagen = "donuts_conchispas_6unidades")
            )
            repositorio.insertAll(productosDeEjemplo)
        }
    }
    
    fun getProducto(id: Int): Flow<Producto> {
        return repositorio.getProducto(id)
    }
}
