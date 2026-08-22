package com.project.posapp.feature.cashier.receivable

import com.project.posapp.model.ReceivableItem
import com.project.posapp.utils.composable.TransactionTableItem
import com.project.posapp.utils.extensions.toRupiah

internal fun String?.toReceivableDueStatus(): String =
    when (this?.lowercase()) {
        "active" -> "Aktif"
        "today" -> "Hari ini"
        "overdue" -> "Terlambat"
        else -> "-"
    }

internal fun ReceivableItem.toTransactionTableItem() =
    TransactionTableItem(
        name = name ?: "-",
        price = (unitPrice ?: 0L).toRupiah(),
        quantity = (quantity ?: 0).toString(),
        subtotal = (
                subtotalAfterDiscount
                    ?: subtotal
                    ?: 0L
                ).toRupiah(),
        subLabel = discount
            ?.takeIf { it > 0L }
            ?.let {
                "Diskon ${it.toRupiah()}"
            }
    )