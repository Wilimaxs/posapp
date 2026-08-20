package com.project.posapp.utils.extensions

import java.text.NumberFormat
import java.util.Locale

private val indonesiaLocale = Locale.forLanguageTag("id-ID")

fun Long.toRupiah(): String {
    return NumberFormat
        .getCurrencyInstance(indonesiaLocale)
        .apply { maximumFractionDigits = 0 }
        .format(this)
        .replace("\u00A0", "")
}

fun String.formatAmount(): String {
    if (isBlank()) return ""

    return toLongOrNull()
        ?.let {
            NumberFormat
                .getNumberInstance(indonesiaLocale)
                .format(it)
        }
        ?: ""
}

fun Long.roundUp(
    multiple: Long
): Long {
    require(multiple > 0)

    return ((this + multiple - 1) / multiple) * multiple
}