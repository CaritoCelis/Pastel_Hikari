package com.example.pastel_hikari.navegacion

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pastel_hikari.presentacion.pantallas.PantallaCarrito
import com.example.pastel_hikari.presentacion.pantallas.PantallaDetalleProducto
import com.example.pastel_hikari.presentacion.pantallas.PantallaInicioSesion
import com.example.pastel_hikari.presentacion.pantallas.PantallaProductos
import com.example.pastel_hikari.presentacion.pantallas.PantallaRegistro

@Composable
fun NavegacionPrincipal() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            PantallaInicioSesion(navController = navController)
        }

        composable("registro") {
            PantallaRegistro(navController = navController)
        }

        composable("home") {
            PantallaProductos(navController = navController)
        }

        composable(
            route = "producto_detalle/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) {
            val productoId = it.arguments?.getInt("productoId")
            requireNotNull(productoId) { "El id del producto no puede ser nulo" }
            
            PantallaDetalleProducto(navController = navController, productoId = productoId)
        }

        // La ruta "carrito" ahora muestra la pantalla del carrito real
        composable("carrito") {
            PantallaCarrito(navController = navController)
        }
    }
}
