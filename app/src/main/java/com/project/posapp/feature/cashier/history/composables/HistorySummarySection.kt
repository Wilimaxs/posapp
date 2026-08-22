package com.project.posapp.feature.cashier.history.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.HistorySummary
import com.project.posapp.utils.composable.AppBadge
import com.project.posapp.utils.composable.AppSummaryCard
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun HistorySummarySection(
    summary: HistorySummary?,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(96.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        RoundedCornerShape(Radius.Medium)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(Radius.Medium)
                    )
                    .padding(Spacing.Standard),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ringkasan gagal dimuat",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                PrimaryButton(
                    text = "Coba Lagi",
                    onClick = onRetry,
                    fillMaxWidth = false,
                    height = 48.dp
                )
            }
        }

        else -> {
            val data = summary ?: HistorySummary()

            val cards = listOf(
                SummaryCardData(
                    title = "Total transaksi",
                    value = data.totalTransactions.toString()
                ),
                SummaryCardData(
                    title = "Total penjualan",
                    value = data.totalSales.toRupiah()
                ),
                SummaryCardData(
                    title = "Pembayaran tunai",
                    value = data.cashPayment.toRupiah()
                ),
                SummaryCardData(
                    title = "Pembayaran QR",
                    value = data.qrisPayment.toRupiah()
                )
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
            ) {
                cards.forEach { card ->
                    AppSummaryCard(
                        title = card.title,
                        value = card.value,
                        modifier = Modifier.weight(1f),
                        trailingContent = {
                            AppBadge(text = "Hari ini")
                        }
                    )
                }
            }
        }
    }
}

private data class SummaryCardData(
    val title: String,
    val value: String
)