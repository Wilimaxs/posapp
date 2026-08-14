package com.project.posapp.utils

import java.text.NumberFormat
import java.util.Locale

fun Long.toRupiah(): String {
    return NumberFormat
        .getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        .apply { maximumFractionDigits = 0 }
        .format(this)
        .replace("\u00A0", "")
}