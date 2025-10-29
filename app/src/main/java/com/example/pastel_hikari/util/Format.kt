package com.example.pastel_hikari.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Función de utilidad para formatear un Double a moneda chilena (CLP).
 * Ej: 10000.0 -> $10.000
 */
fun formatCurrency(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    return format.format(value)
}
