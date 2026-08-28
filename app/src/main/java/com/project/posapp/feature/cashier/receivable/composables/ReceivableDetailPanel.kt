package com.project.posapp.feature.cashier.receivable.composables

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.receivable.toReceivableDueStatus
import com.project.posapp.model.ReceivableDetail
import com.project.posapp.utils.composable.AppBadge
import com.project.posapp.utils.composable.transaction.AppDetailInformation
import com.project.posapp.utils.composable.transaction.AppInstallmentHistory
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.composable.transaction.InstallmentHistoryItem
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.extensions.toLongDisplayDate
import com.project.posapp.utils.extensions.toLongDisplayDateFromDateTime
import com.project.posapp.utils.extensions.toLongDisplayDateTime
import com.project.posapp.utils.extensions.toPaymentMethodLabel
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun ReceivableDetailPanel(
    detail: ReceivableDetail?,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onShowTransaction: () -> Unit,
    onReceivePayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
    ) {
        AppState(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Standard),
            isLoading = isLoading,
            errorMessage = errorMessage,
            isEmpty = detail == null,
            errorTitle = "Detail piutang gagal dimuat",
            emptyTitle = "Pilih piutang",
            emptyDescription =
                "Pilih piutang untuk melihat detail.",
            onAction = onRetry
        ) {
            val receivableDetail = requireNotNull(detail)

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ReceivableDetailHeader(
                    detail = receivableDetail
                )

                HorizontalDivider(
                    color =
                        MaterialTheme.colorScheme.outlineVariant
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(Spacing.Standard),
                    verticalArrangement =
                        Arrangement.spacedBy(Spacing.Large)
                ) {
                    ReceivableCustomerInformation(
                        detail = receivableDetail
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                Spacing.Standard
                            )
                    ) {
                        OriginTransactionCard(
                            detail = receivableDetail,
                            onShowTransaction =
                                onShowTransaction,
                            modifier = Modifier.weight(1f)
                        )

                        ReceivableBalanceCard(
                            detail = receivableDetail,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    AppInstallmentHistory(
                        initialPayment =
                            InstallmentHistoryItem(
                                title = "Uang Muka (DP)",
                                amount =
                                    receivableDetail
                                        .initialPayment
                                        ?: 0L,
                                metadata =
                                    listOfNotNull(
                                        receivableDetail
                                            .createdAt
                                            .toLongDisplayDateTime(),
                                        receivableDetail
                                            .paymentMethod
                                            .toPaymentMethodLabel(),
                                        receivableDetail
                                            .cashier
                                            ?.name
                                            ?.let {
                                                "Kasir: $it"
                                            }
                                    ).joinToString(" • ")
                            ),
                        installments =
                            receivableDetail
                                .receivablePayments
                                .mapIndexed { index, payment ->
                                    InstallmentHistoryItem(
                                        title = payment.notes
                                            ?.takeIf(
                                                String::isNotBlank
                                            )
                                            ?: "Pembayaran cicilan ${index + 1}",
                                        amount =
                                            payment.amount ?: 0L,
                                        metadata =
                                            listOfNotNull(
                                                payment
                                                    .createdAt
                                                    .toLongDisplayDateTime(),
                                                payment.user
                                                    ?.name
                                                    ?.let {
                                                        "Kasir: $it"
                                                    }
                                            ).joinToString(
                                                " • "
                                            )
                                    )
                                }
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    PrimaryButton(
                        text = "Cetak Riwayat",
                        onClick = {},
                        fillMaxWidth = false,
                        reverse = true,
                        height = 44.dp,
                        textStyle = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(
                            horizontal = Spacing.Tight,
                            vertical = Spacing.Tight
                        )
                    )
                    PrimaryButton(
                        text = "Terima Pembayaran",
                        onClick = onReceivePayment,
                        fillMaxWidth = false,
                        height = 44.dp,
                        textStyle = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(
                            horizontal = Spacing.Tight,
                            vertical = Spacing.Tight
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceivableDetailHeader(
    detail: ReceivableDetail
) {
    val accentColor =
        detail.dueStatus.receivableDueStatusColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Spacing.Standard,
                vertical = Spacing.Compact
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(Spacing.Standard)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(Spacing.Tight),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = "DETAIL PIUTANG",
                    style =
                        MaterialTheme.typography.labelMedium,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                AppBadge(
                    text = detail.dueStatus
                        .toReceivableDueStatus(),
                    containerColor =
                        accentColor.copy(alpha = 0.12f),
                    contentColor = accentColor
                )
            }

            Text(
                text = "Jatuh tempo ${
                    detail.dueDate.toLongDisplayDate()
                }",
                style = MaterialTheme.typography.labelMedium,
                color = accentColor
            )
        }

        Text(
            text = detail.invoiceNumber ?: "-",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ReceivableCustomerInformation(
    detail: ReceivableDetail
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppDetailInformation(
            label = "Nama member",
            value = detail.customer?.name ?: "-",
        )

        AppDetailInformation(
            label = "Nomor telepon",
            value = detail.customer?.phone ?: "-",
        )

        AppDetailInformation(
            label = "Alamat",
            value = detail.customer?.address ?: "-",
        )
    }
}

@Composable
private fun OriginTransactionCard(
    detail: ReceivableDetail,
    onShowTransaction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceContainerLow,
                RoundedCornerShape(Radius.Medium)
            )
            .border(
                width = 1.dp,
                color =
                    MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Radius.Medium)
            )
            .padding(Spacing.Standard),
        verticalArrangement =
            Arrangement.spacedBy(Spacing.Tight)
    ) {
        Row(
            horizontalArrangement =
                Arrangement.spacedBy(Spacing.Tight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Asal Transaksi",
                style = MaterialTheme.typography.titleMedium
            )
        }

        CompactInformationRow(
            label = "Tanggal",
            value = detail.createdAt
                .toLongDisplayDateFromDateTime()
        )

        CompactInformationRow(
            label = "Kasir",
            value = detail.cashier?.name ?: "-"
        )

        CompactInformationRow(
            label = "Item",
            value = "${
                detail.items.sumOf {
                    it.quantity ?: 0
                }
            } barang"
        )

        CompactInformationRow(
            label = "Metode DP",
            value = detail.paymentMethod
                .toPaymentMethodLabel()
        )

        PrimaryButton(
            text = "Lihat transaksi",
            onClick = onShowTransaction,
            reverse = true,
            height = 44.dp,
            textStyle = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun ReceivableBalanceCard(
    detail: ReceivableDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer
                    .copy(alpha = 0.08f),
                RoundedCornerShape(Radius.Medium)
            )
            .border(
                width = 1.dp,
                color =
                    MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Radius.Medium)
            )
            .padding(Spacing.Standard),
        verticalArrangement =
            Arrangement.spacedBy(Spacing.Tight)
    ) {
        CompactInformationRow(
            label = "Total transaksi",
            value = (
                    detail.totalAfterDiscount ?: 0L
                    ).toRupiah()
        )

        CompactInformationRow(
            label = "Uang muka (DP)",
            value = "-${
                (detail.initialPayment ?: 0L).toRupiah()
            }"
        )

        CompactInformationRow(
            label = "Cicilan dibayar",
            value = "-${
                (detail.installmentTotal ?: 0L).toRupiah()
            }"
        )

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = Spacing.Tight
            ),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Sisa Piutang",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = (
                        detail.remainingBalance ?: 0L
                        ).toRupiah(),
                style =
                    MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CompactInformationRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}