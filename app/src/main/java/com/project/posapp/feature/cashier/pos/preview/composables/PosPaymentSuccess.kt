package com.project.posapp.feature.cashier.pos.preview.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.PosPayment
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.toRupiah
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PosPaymentSuccess(
    payment: PosPayment,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPartial =
        (payment.remainingBalance ?: 0L) > 0L

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Large
        )
    ) {
        SuccessHeader(
            payment = payment,
            isPartial = isPartial
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                Spacing.Standard
            )
        ) {
            PaymentFinancialCard(
                payment = payment,
                isPartial = isPartial,
                modifier = Modifier.weight(1f)
            )

            TransactionDetailCard(
                payment = payment,
                modifier = Modifier.weight(1f)
            )
        }

        ReceiptActions()

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(
                size = Radius.Medium
            )
        ) {
            Text(
                text = "Selesai",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun SuccessHeader(
    payment: PosPayment,
    isPartial: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            Spacing.Standard
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                Spacing.Micro
            )
        ) {
            Text(
                text = "Pembayaran berhasil",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (isPartial) {
                    "Pembayaran awal diterima dan sisa transaksi tercatat sebagai piutang."
                } else {
                    "Transaksi selesai dan pembayaran berhasil diterima."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "No. Transaksi",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = payment.invoiceNumber ?: "-",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun PaymentFinancialCard(
    payment: PosPayment,
    isPartial: Boolean,
    modifier: Modifier = Modifier
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
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
                    .copy(alpha = 0.18f),
                shape = RoundedCornerShape(
                    size = Radius.Medium
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary
                    .copy(alpha = 0.25f),
                shape = RoundedCornerShape(
                    size = Radius.Medium
                )
            )
            .padding(all = Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Standard
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                Spacing.Micro
            )
        ) {
            Text(
                text = mainLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = mainAmount.toRupiah(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        PaymentDetailRow(
            label = "Total transaksi",
            value = (
                    payment.totalAfterDiscount ?: 0L
                    ).toRupiah()
        )

        PaymentDetailRow(
            label = if (isPartial) {
                "Uang muka"
            } else {
                "Uang diterima"
            },
            value = if (isPartial) {
                (payment.initialPayment ?: 0L)
                    .toRupiah()
            } else {
                (
                        (payment.initialPayment ?: 0L) +
                                (payment.changeAmount ?: 0L)
                        ).toRupiah()
            }
        )

        if (isPartial) {
            PaymentDetailRow(
                label = "Jatuh tempo",
                value = payment.dueDate
                    .toDisplayDate()
            )
        }
    }
}

@Composable
private fun TransactionDetailCard(
    payment: PosPayment,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(
                    size = Radius.Medium
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(
                    size = Radius.Medium
                )
            )
            .padding(all = Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Standard
        )
    ) {
        Text(
            text = "Detail transaksi",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        CustomerInformation(
            payment = payment
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        InformationBlock(
            label = "Metode pembayaran",
            value = payment.paymentMethod
                .toPaymentMethod()
        )

        InformationBlock(
            label = "Kasir",
            value = payment.user?.name ?: "-"
        )

        InformationBlock(
            label = "Waktu transaksi",
            value = payment.createdAt
                .toDisplayDateTime()
        )
    }
}

@Composable
private fun CustomerInformation(
    payment: PosPayment
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Micro
        )
    ) {
        Text(
            text = "Pelanggan",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                Spacing.Tight
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = payment.customer?.name
                    ?: "Guest",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            if (
                payment.customerType.equals(
                    other = "member",
                    ignoreCase = true
                )
            ) {
                Text(
                    text = "Member",
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(
                                percent = 50
                            )
                        )
                        .padding(
                            horizontal = Spacing.Tight,
                            vertical = Spacing.Micro
                        ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun InformationBlock(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Micro
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ReceiptActions() {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Tight
        )
    ) {
        Text(
            text = "Struk",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                Spacing.Standard
            )
        ) {
            ReceiptButton(
                title = "Cetak Struk",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Print,
                        contentDescription = null
                    )
                },
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            ReceiptButton(
                title = "Kirim Struk",
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = null
                    )
                },
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ReceiptButton(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(
            size = Radius.Medium
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                Spacing.Standard
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun PaymentDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun String?.toPaymentMethod(): String =
    when (this?.lowercase()) {
        "cash" -> "Tunai"
        "qr" -> "QR"
        else -> "-"
    }

private fun String?.toDisplayDateTime(): String {
    if (this == null) {
        return "-"
    }

    return runCatching {
        Instant
            .parse(this)
            .atZone(
                ZoneId.systemDefault()
            )
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy • HH:mm",
                    Locale.forLanguageTag("id-ID")
                )
            )
    }.getOrDefault("-")
}

private fun String?.toDisplayDate(): String {
    if (this == null) {
        return "-"
    }

    return runCatching {
        LocalDate
            .parse(this)
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy",
                    Locale.forLanguageTag("id-ID")
                )
            )
    }.getOrDefault(this)
}