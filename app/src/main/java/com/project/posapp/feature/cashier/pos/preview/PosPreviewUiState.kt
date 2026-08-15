package com.project.posapp.feature.cashier.pos.preview

import com.project.posapp.model.PosCheckoutPreview

data class PosPreviewUiState(
    val isLoading: Boolean = false,
    val preview: PosCheckoutPreview? = null,
    val remainingSeconds: Long? = null,

    val paymentSchema: PosPaymentScheme = PosPaymentScheme.FULL,
    val paymentMethod: PosPaymentMethod? = null,

    val downPayment: String = "",
    val dueDate: String? = null,

    val errorMessage: String? = null
) {
    val isVisible: Boolean get() = isLoading || preview != null || errorMessage != null
    val downPaymentAmount: Long get() = downPayment.toLongOrNull() ?: 0L
    val remainingReceivable: Long
        get() = ((preview?.totalAfterDiscount ?: 0L) - downPaymentAmount).coerceAtLeast(0L)
    val canContinue: Boolean get() = preview?.saleId != null && paymentMethod != null
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

enum class PosPaymentMethod {
    CASH,
    QR
}