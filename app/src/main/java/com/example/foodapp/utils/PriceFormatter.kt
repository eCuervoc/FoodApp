package com.example.foodapp.utils

import java.text.NumberFormat
import java.util.Locale

object PriceFormatter {
    private val formatter: NumberFormat = NumberFormat.getNumberInstance(Locale("es", "CO"))

    fun pesos(value: Double): String = "$ ${formatter.format(value.toInt())}"
}
