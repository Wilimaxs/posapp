package com.project.posapp.feature.cashier.pos.preview.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.PosPayment
import com.project.posapp.ui.theme.Radius
import com.project.posapp.utils.toRupiah
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PosPaymentSuccess(
    payment: PosPayment,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPartial = (payment.remainingBalance ?: 0L) > 0L

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {
        SuccessHeader(isPartial = isPartial)
        PaymentResultCard(
            payment = payment,
            isPartial = isPartial
        )
        TransactionInformation(payment = payment)

        Button(
            onClick = onFinish,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Selesai"
            )
        }
    }
}

@Composable
private fun SuccessHeader(
    isPartial: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "Pembayaran berhasil",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (isPartial) {
                "Pembayaran awal berhasil diterima dan sisa tagihan tercatat sebagai piutang."
            } else {
                "Transaksi telah selesai dan pembayaran berhasil diterima."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PaymentResultCard(
    payment: PosPayment,
    isPartial: Boolean
) {
    val mainLabel = if (isPartial) {
        "Sisa piutang"
    } else {
        "Kembalian"
    }

    val mainAmount = if (isPartial) {
        payment.remainingBalance ?: 0L
    } else {
        payment.changeAmount ?: 0L
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f),
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .padding(all = Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        Text(
            text = mainLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = mainAmount.toRupiah(),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = Spacing.Tight),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        PaymentDetailRow(
            label = "Total pembayaran",
            value = (payment.totalAfterDiscount ?: 0L).toRupiah()
        )

        PaymentDetailRow(
            label = if (isPartial) {
                "Uang muka"
            } else {
                "Uang diterima"
            },
            value = if (isPartial) {
                (payment.initialPayment ?: 0L).toRupiah()
            } else {
                ((payment.initialPayment ?: 0L) + (payment.changeAmount ?: 0L)).toRupiah()
            }
        )
        if (isPartial) {
            PaymentDetailRow(
                label = "Jatuh tempo",
                value = payment.dueDate?.toDisplayDate() ?: "-"
            )
        }
    }
}

@Composable
private fun TransactionInformation(
    payment: PosPayment
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        PaymentDetailRow(
            label = "No. Transaksi",
            value = payment.invoiceNumber ?: "-"
        )

        PaymentDetailRow(
            label = "Pelanggan",
            value = payment.customer?.name ?: "Guest"
        )

        PaymentDetailRow(
            label = "Tipe pelanggan",
            value = when (
                payment.customerType?.lowercase()
            ) {
                "member" -> "Member"
                else -> "Guest"
            }
        )

        PaymentDetailRow(
            label = "Metode bayar",
            value = when (payment.paymentMethod?.lowercase()) {
                "cash" -> "Tunai"
                "qr" -> "QR"
                else -> "-"
            }
        )
        PaymentDetailRow(
            label = "Kasir",
            value = payment.user?.name ?: "-"
        )

        PaymentDetailRow(
            label = "Waktu",
            value = payment.createdAt.toDisplayDateTime()
        )
    }
}

@Composable
private fun PaymentDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun String?.toDisplayDateTime(): String {
    if (this == null) {
        return "-"
    }

    return runCatching {
        Instant
            .parse(this)
            .atZone(ZoneId.systemDefault())
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy • HH:mm",
                    Locale.forLanguageTag("id-ID")
                )
            )
    }.getOrDefault("-")
}

private fun String.toDisplayDate(): String {
    return runCatching {
        java.time.LocalDate
            .parse(this)
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy",
                    Locale.forLanguageTag("id-ID")
                )
            )
    }.getOrDefault(this)
}