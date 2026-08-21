package com.project.posapp.feature.cashier.history.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.history.toHistoryPaymentMethod
import com.project.posapp.model.HistoryDetail
import com.project.posapp.utils.extensions.toLongDisplayDateTime
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun HistoryInstallmentHistory(
    detail: HistoryDetail,
    modifier: Modifier = Modifier
) {
    val payment = detail.payment ?: return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        Text(
            text = "Riwayat Pembayaran Cicilan",
            style = MaterialTheme.typography.titleMedium
        )

        InstallmentRow(
            title = "Uang Muka (DP)",
            amount = payment.initialPayment ?: 0L,
            metadata = listOfNotNull(
                detail.createdAt.toLongDisplayDateTime(),
                payment.method.toHistoryPaymentMethod(),
                detail.user?.name?.let {
                    "Kasir: $it"
                }
            ).joinToString(" • ")
        )

        detail.receivablePayments.forEachIndexed { index, item ->
            InstallmentRow(
                title = item.notes
                    ?.takeIf(String::isNotBlank)
                    ?: "Pembayaran cicilan ${index + 1}",
                amount = item.amount ?: 0L,
                metadata = listOfNotNull(
                    item.createdAt.toLongDisplayDateTime(),
                    item.user?.name?.let {
                        "Kasir: $it"
                    }
                ).joinToString(" • ")
            )
        }

        if (detail.receivablePayments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer,
                        RoundedCornerShape(Radius.Default)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(Radius.Default)
                    )
                    .padding(Spacing.Standard),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada pembayaran cicilan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InstallmentRow(
    title: String,
    amount: Long,
    metadata: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            Spacing.Standard
        ),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.width(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        CircleShape
                    )
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .size(width = 1.dp, height = 56.dp)
                    .background(
                        MaterialTheme.colorScheme.outlineVariant
                    )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(Radius.Default)
                )
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(Radius.Default)
                )
                .padding(Spacing.Standard),
            verticalArrangement = Arrangement.spacedBy(
                Spacing.Micro
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = amount.toRupiah(),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Text(
                text = metadata,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}