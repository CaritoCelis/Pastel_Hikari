package com.example.pastel_hikari.datos

import android.content.Context
import androidx.room.*
import com.example.pastel_hikari.datos.base_de_datos.dao.BoletaDao
import com.example.pastel_hikari.datos.base_de_datos.dao.CarritoDao
import com.example.pastel_hikari.datos.base_de_datos.dao.ProductoDao
import com.example.pastel_hikari.datos.base_de_datos.dao.UsuarioDao
import com.example.pastel_hikari.modelo.Boleta
import com.example.pastel_hikari.modelo.ItemCarrito
import com.example.pastel_hikari.modelo.Producto
import com.example.pastel_hikari.modelo.Usuario
import com.example.pastel_hikari.util.Converters

@Database(
    entities = [Usuario::class, Producto::class, ItemCarrito::class, Boleta::class],
    version = 3, // Incrementamos la versión
    exportSchema = false
)
@TypeConverters(Converters::class) // Le decimos a Room que use nuestros conversores
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun boletaDao(): BoletaDao

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
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
