package com.project.posapp.feature.cashier.receivable.composables

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Success
import com.project.posapp.core.theme.Warning
import com.project.posapp.model.ReceivableSummary
import com.project.posapp.utils.composable.AppBadge
import com.project.posapp.utils.composable.AppSummaryCard
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun ReceivableSummarySection(
    summary: ReceivableSummary?,
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
                        MaterialTheme.colorScheme
                            .surfaceContainerLowest,
                        RoundedCornerShape(Radius.Medium)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme
                            .outlineVariant,
                        shape = RoundedCornerShape(Radius.Medium)
                    )
                    .padding(Spacing.Standard),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ringkasan gagal dimuat",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = errorMessage,
                        style =
                            MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme
                            .onSurfaceVariant
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
            val data = summary ?: ReceivableSummary()

            val cards = listOf(
                ReceivableSummaryCardData(
                    title = "Total piutang aktif",
                    value = data.totalActive.amount.toRupiah(),
                    supportingText =
                        "${data.totalActive.count} tagihan belum lunas",
                    icon = Icons.Outlined.AccountBalanceWallet,
                    accentColor =
                        MaterialTheme.colorScheme.primary
                ),
                ReceivableSummaryCardData(
                    title = "Jatuh tempo hari ini",
                    value = data.dueToday.amount.toRupiah(),
                    supportingText =
                        "${data.dueToday.count} tagihan",
                    icon = Icons.Outlined.Event,
                    accentColor = Warning
                ),
                ReceivableSummaryCardData(
                    title = "Terlambat",
                    value = data.overdue.amount.toRupiah(),
                    supportingText =
                        "${data.overdue.count} tagihan (butuh tindakan)",
                    icon = Icons.Outlined.ErrorOutline,
                    accentColor =
                        MaterialTheme.colorScheme.error
                ),
                ReceivableSummaryCardData(
                    title = "Pembayaran hari ini",
                    value = data.paymentsToday.amount.toRupiah(),
                    supportingText =
                        "${data.paymentsToday.count} transaksi pelunasan",
                    icon = Icons.Outlined.Payments,
                    accentColor = Success
                )
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(Spacing.Standard)
            ) {
                cards.forEach { card ->
                    AppSummaryCard(
                        title = card.title,
                        value = card.value,
                        supportingText = card.supportingText,
                        valueColor = card.accentColor,
                        supportingTextColor = card.accentColor,
                        modifier = Modifier.weight(1f),
                        trailingContent = {
                            AppBadge(
                                containerColor =
                                    card.accentColor.copy(
                                        alpha = 0.12f
                                    ),
                                contentColor = card.accentColor
                            ) {
                                Icon(
                                    imageVector = card.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = card.accentColor
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

private data class ReceivableSummaryCardData(
    val title: String,
    val value: String,
    val supportingText: String,
    val icon: ImageVector,
    val accentColor: Color
)