package com.project.posapp.feature.cashier.pos.preview.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.PosPayment
import com.project.posapp.utils.composable.AppResultDialog
import com.project.posapp.utils.composable.AppResultNotification
import com.project.posapp.utils.composable.AppResultType
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun PosPaymentDialog(
    payment: PosPayment,
    onPrintReceipt: (PosPayment) -> Unit,
    onSendReceipt: (PosPayment) -> Unit,
    onFinish: () -> Unit
) {
    AppResultDialog(
        type = AppResultType.SUCCESS,
        title = "Pembayaran Berhasil",
        message = payment.successMessage(),
        notification = payment.resultNotification(),
        primaryButtonText = "Selesai",
        onPrimaryClick = onFinish,
        content = {
            Text(
                text = payment.invoiceNumber ?: "-",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
            ) {
                PrimaryButton(
                    text = "Cetak Struk",
                    icon = Icons.Outlined.Print,
                    onClick = {
                        onPrintReceipt(payment)
                    },
                    modifier = Modifier.weight(1f),
                    reverse = true,
                    fillMaxWidth = false,
                    height = 64.dp
                )

                PrimaryButton(
                    text = "Kirim Struk",
                    icon = Icons.AutoMirrored.Outlined.Send,
                    onClick = {
                        onSendReceipt(payment)
                    },
                    modifier = Modifier.weight(1f),
                    reverse = true,
                    fillMaxWidth = false,
                    height = 64.dp
                )
            }
        }
    )
}

private fun PosPayment.resultNotification(): AppResultNotification {
    val total = totalAfterDiscount ?: 0L
    val initial = initialPayment ?: 0L
    val change = changeAmount ?: 0L

    val remaining = (
            remainingBalance
                ?: (total - initial)
            ).coerceAtLeast(0L)

    return when {
        change > 0L -> {
            AppResultNotification(
                label = "Kembalian",
                value = change.toRupiah()
            )
        }

        remaining > 0L || initial < total -> {
            AppResultNotification(
                label = "Sisa piutang",
                value = remaining.toRupiah()
            )
        }

        else -> {
            AppResultNotification(
                label = "Status pembayaran",
                value = "Lunas"
            )
        }
    }
}

private fun PosPayment.successMessage(): String {
    val method = when (paymentMethod?.lowercase()) {
        "cash" -> "tunai"
        "qr" -> "QR"
        else -> null
    }

    return if (method != null) {
        "Pembayaran $method berhasil diproses."
    } else {
        "Pembayaran berhasil diproses."
    }
}