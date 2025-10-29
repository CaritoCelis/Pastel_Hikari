package com.example.pastel_hikari.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pastel_hikari.presentacion.pantallas.*

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

        composable("carrito") {
            PantallaCarrito(navController = navController)
        }

        // Ruta para la pantalla de la boleta
        composable(
            route = "boleta/{boletaId}",
            arguments = listOf(navArgument("boletaId") { type = NavType.LongType })
        ) {
            val boletaId = it.arguments?.getLong("boletaId")
            requireNotNull(boletaId) { "El id de la boleta no puede ser nulo" }

            PantallaBoleta(navController = navController, boletaId = boletaId)
        }
    }
}
