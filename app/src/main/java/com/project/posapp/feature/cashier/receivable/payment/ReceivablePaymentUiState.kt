package com.project.posapp.feature.cashier.receivable.payment

import com.project.posapp.model.ReceivableDetail
import com.project.posapp.model.ReceivablePayment

data class ReceivablePaymentUiState(
    val detail: ReceivableDetail? = null,

    val amount: String = "",
    val notes: String = "",

    val isLoading: Boolean = false,
    val payment: ReceivablePayment? = null,
    val errorMessage: String? = null
) {
    val amountValue: Long
        get() = amount.toLongOrNull() ?: 0L

    val remainingBalance: Long
        get() = detail?.remainingBalance ?: 0L

    val remainingAfterPayment: Long
        get() = (remainingBalance - amountValue)
            .coerceAtLeast(0L)

    val amountError: String?
        get() = when {
            amount.isBlank() -> null

            amountValue <= 0L ->
                "Nominal pembayaran harus lebih dari Rp0."

            amountValue > remainingBalance ->
                "Nominal tidak boleh melebihi sisa piutang."

            else -> null
        }

    val canSubmit: Boolean
        get() =
            detail?.saleId != null &&
                    amountValue > 0L &&
                    amountValue <= remainingBalance &&
                    !isLoading
}