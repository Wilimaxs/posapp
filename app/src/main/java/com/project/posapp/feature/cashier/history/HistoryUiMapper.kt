package com.project.posapp.feature.cashier.history

import com.project.posapp.model.HistoryItem
import com.project.posapp.utils.composable.TransactionTableItem
import com.project.posapp.utils.extensions.toRupiah

internal fun String?.toHistoryCustomerType(): String =
    when (this?.lowercase()) {
        "member" -> "Member"
        "guest" -> "Guest"
        else -> "-"
    }

internal fun String?.toHistoryPaymentStatus(): String =
    when (this?.lowercase()) {
        "paid" -> "Lunas"
        "partial" -> "Sebagian"
        else -> "-"
    }

internal fun HistoryItem.toTransactionTableItem() =
    TransactionTableItem(
        name = name ?: "-",
        price = (unitPrice ?: 0L).toRupiah(),
        quantity = (quantity ?: 0).toString(),
        subtotal = (
                subtotalAfterDiscount
                    ?: subtotal
                    ?: 0L
                ).toRupiah(),
        subLabel = discount?.let {
            buildString {
                append(it.name ?: "Diskon")

                it.value?.let { value ->
                    append(" • Diskon ${value.toRupiah()}")
                }
            }
        }
    )