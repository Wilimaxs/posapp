package com.project.posapp.utils.extensions

fun String?.toPaymentMethodLabel(): String =
    when (this?.lowercase()) {
        "cash" -> "Tunai"
        "qr", "qris" -> "QR"
        else -> "-"
    }