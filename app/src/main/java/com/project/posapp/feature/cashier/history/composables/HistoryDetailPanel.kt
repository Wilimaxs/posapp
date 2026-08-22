package com.project.posapp.feature.cashier.history.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Success
import com.project.posapp.core.theme.Warning
import com.project.posapp.utils.extensions.toPaymentMethodLabel
import com.project.posapp.feature.cashier.history.toHistoryPaymentStatus
import com.project.posapp.feature.cashier.history.toTransactionTableItem
import com.project.posapp.model.HistoryDetail
import com.project.posapp.utils.composable.AppBadge
import com.project.posapp.utils.composable.transaction.AppDetailInformation
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.composable.TransactionDetailTable
import com.project.posapp.utils.extensions.toLongDisplayDateFromDateTime
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun HistoryDetailPanel(
    detail: HistoryDetail?,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onPrintReceipt: ((HistoryDetail) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest,
                RoundedCornerShape(Radius.Medium)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(Radius.Medium)
            )
    ) {
        AppState(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Standard),
            isLoading = isLoading,
            errorMessage = errorMessage,
            isEmpty = detail == null,
            errorTitle = "Detail transaksi gagal dimuat",
            emptyTitle = "Pilih transaksi",
            emptyDescription = "Pilih transaksi untuk melihat detail.",
            onAction = onRetry
        ) {
            val historyDetail = requireNotNull(detail)

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HistoryDetailHeader(
                    detail = historyDetail
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(Spacing.Standard),
                    verticalArrangement = Arrangement.spacedBy(
                        Spacing.Large
                    )
                ) {
                    HistoryDetailInformation(
                        detail = historyDetail
                    )

                    TransactionDetailTable(
                        title = "Riwayat Penjualan",
                        items = historyDetail.items.map {
                            it.toTransactionTableItem()
                        },
                        subtotalLabel = "Subtotal semua produk",
                        subtotalValue = (
                                historyDetail.totalBeforeDiscount
                                    ?: 0L
                                ).toRupiah(),
                        discountLabel = "Total diskon",
                        discountValue = "-${
                            (historyDetail.totalDiscount ?: 0L)
                                .toRupiah()
                        }",
                        showDiscount =
                            (historyDetail.totalDiscount ?: 0L) > 0L,
                        totalLabel = "Total",
                        totalValue = (
                                historyDetail.totalAfterDiscount
                                    ?: 0L
                                ).toRupiah()
                    )

                    if (
                        historyDetail.payment?.paymentStatus.equals(
                            other = "partial",
                            ignoreCase = true
                        )
                    ) {
                        HistoryInstallmentHistory(
                            detail = historyDetail
                        )
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                PrimaryButton(
                    text = "Cetak Ulang Struk",
                    onClick = {
                        onPrintReceipt?.invoke(historyDetail)
                    },
                    enabled = onPrintReceipt != null,
                    reverse = true,
                    height = 44.dp,
                    textStyle = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(
                        horizontal = Spacing.Standard,
                        vertical = Spacing.Tight
                    )
                )
            }
        }
    }
}

@Composable
private fun HistoryDetailHeader(
    detail: HistoryDetail
) {
    val isPartial = detail.payment?.paymentStatus.equals(
        other = "partial",
        ignoreCase = true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Spacing.Standard,
                vertical = Spacing.Compact
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Standard
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing.Tight
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DETAIL TRANSAKSI",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                AppBadge(
                    text = detail.payment?.paymentStatus
                        .toHistoryPaymentStatus(),
                    containerColor = if (isPartial) {
                        Warning.copy(alpha = 0.12f)
                    } else {
                        Success.copy(alpha = 0.12f)
                    },
                    contentColor = if (isPartial) {
                        Warning
                    } else {
                        Success
                    }
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing.Tight
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = detail.store?.name ?: "-",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = detail.createdAt.toLongDisplayDateFromDateTime(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Text(
            text = detail.invoiceNumber ?: "-",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun HistoryDetailInformation(
    detail: HistoryDetail
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            Spacing.Large
        )
    ) {
        AppDetailInformation(
            label = "Kasir",
            value = detail.user?.name ?: "-",
            modifier = Modifier.weight(1f)
        )

        AppDetailInformation(
            label = "Metode pembayaran",
            value = detail.payment?.method
                .toPaymentMethodLabel(),
            modifier = Modifier.weight(1f)
        )

        AppDetailInformation(
            label = "Pelanggan",
            value = detail.customer?.let {
                buildString {
                    append(it.name ?: "-")

                    it.phone?.let { phone ->
                        append(" • $phone")
                    }
                }
            } ?: "Guest",
            modifier = Modifier.weight(1.4f)
        )
    }
}