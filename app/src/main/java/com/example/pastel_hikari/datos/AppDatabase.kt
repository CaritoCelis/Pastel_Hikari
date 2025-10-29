package com.example.pastel_hikari.datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pastel_hikari.datos.base_de_datos.dao.CarritoDao
import com.example.pastel_hikari.datos.base_de_datos.dao.ProductoDao
import com.example.pastel_hikari.datos.base_de_datos.dao.UsuarioDao
import com.example.pastel_hikari.modelo.ItemCarrito
import com.example.pastel_hikari.modelo.Producto
import com.example.pastel_hikari.modelo.Usuario

@Database(entities = [Usuario::class, Producto::class, ItemCarrito::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pastel_hikari_database"
                )
                .fallbackToDestructiveMigration() // Añadido para manejar actualizaciones de versión
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
