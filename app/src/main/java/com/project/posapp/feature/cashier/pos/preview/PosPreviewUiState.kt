package com.project.posapp.feature.cashier.pos.preview

import com.project.posapp.model.PosCheckoutPreview
import com.project.posapp.model.PosPayment
import com.project.posapp.utils.extensions.toLocalDateOrNull
import java.time.LocalDate

data class PosPreviewUiState(
    val isLoading: Boolean = false,
    val preview: PosCheckoutPreview? = null,
    val remainingSeconds: Long? = null,

    val paymentSchema: PosPaymentScheme = PosPaymentScheme.FULL,
    val paymentMethod: PosPaymentMethod? = null,

    val downPayment: String = "",
    val dueDate: String? = null,

    val cashReceived: String = "",

    // KHUSUS PAYMENT
    val isPaymentLoading: Boolean = false,
    val payment: PosPayment? = null,
    val paymentErrorMessage: String? = null,

    // CANCEL PREVIEW
    val showCancelConfirmation: Boolean = false,
    val isCancelLoading: Boolean = false,
    val cancelErrorMessage: String? = null,
    val cancelSuccess: Boolean = false,
    val resetTransactionAfterCancel: Boolean = false,

    val errorMessage: String? = null
) {
    val downPaymentAmount: Long get() = downPayment.toLongOrNull() ?: 0L
    val remainingReceivable: Long
        get() = ((preview?.totalAfterDiscount ?: 0L) - downPaymentAmount).coerceAtLeast(0L)
    val cashReceivedAmount: Long get() = cashReceived.toLongOrNull() ?: 0L
    val paymentAmount: Long
        get() = when (paymentSchema) {
            PosPaymentScheme.FULL -> preview?.totalAfterDiscount ?: 0L
            PosPaymentScheme.PARTIAL -> downPaymentAmount
        }
    val paymentRequestAmount: Long
        get() = when {
            paymentSchema == PosPaymentScheme.PARTIAL -> downPaymentAmount
            paymentMethod == PosPaymentMethod.CASH -> cashReceivedAmount
            else -> paymentAmount
        }
    val canContinue: Boolean
        get() {
            val total = preview?.totalAfterDiscount

            if (preview?.saleId == null || total == null || paymentMethod == null || remainingSeconds == 0L) {
                return false
            }

            if (paymentSchema == PosPaymentScheme.PARTIAL) {
                val isDownPaymentValid = downPaymentAmount in 1..<total

                val isDueDateValid = dueDate.toLocalDateOrNull()?.isBefore(LocalDate.now()) == false

                return isDownPaymentValid && isDueDateValid
            }

            return when (paymentMethod) {
                PosPaymentMethod.CASH -> cashReceivedAmount >= paymentAmount
                PosPaymentMethod.QR -> true
            }
        }
    val countdownText: String
        get() {
            val totalSeconds = remainingSeconds ?: return "--:--"
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }
}

enum class PosPaymentScheme {
    FULL,
    PARTIAL
}

enum class PosPaymentMethod(
    val value: String
) {
    CASH("cash"),
    QR("qr")
}