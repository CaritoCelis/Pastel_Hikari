package com.example.pastel_hikari.util

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    return format.format(value)
}
