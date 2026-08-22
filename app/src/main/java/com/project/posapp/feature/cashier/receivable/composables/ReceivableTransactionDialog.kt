package com.project.posapp.feature.cashier.receivable.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.receivable.toTransactionTableItem
import com.project.posapp.model.ReceivableDetail
import com.project.posapp.utils.composable.AppDialog
import com.project.posapp.utils.composable.TransactionDetailTable
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun ReceivableTransactionDialog(
    detail: ReceivableDetail,
    onDismiss: () -> Unit
) {
    AppDialog(
        onDismiss = onDismiss,
        widthFraction = 0.56f,
        maxWidth = 760.dp,
        maxHeight = 760.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.Standard),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(Spacing.Micro)
                ) {
                    Text(
                        text = "Riwayat Penjualan",
                        style = MaterialTheme.typography
                            .headlineSmall
                    )

                    Text(
                        text = "Cek transaksi asal, tidak termasuk pembayaran cicilan.",
                        style =
                            MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme
                            .onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Tutup"
                    )
                }
            }

            HorizontalDivider(
                color =
                    MaterialTheme.colorScheme.outlineVariant
            )

            Column(
                modifier = Modifier.padding(Spacing.Large),
                verticalArrangement =
                    Arrangement.spacedBy(Spacing.Large)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme
                                .surfaceContainerLow,
                            RoundedCornerShape(Radius.Default)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme
                                .outlineVariant,
                            shape = RoundedCornerShape(
                                Radius.Default
                            )
                        )
                        .padding(Spacing.Standard),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(
                                Spacing.Micro
                            )
                    ) {
                        Text(
                            text = detail.customer?.name ?: "-",
                            style = MaterialTheme.typography
                                .titleMedium
                        )

                        Text(
                            text = detail.customer?.phone ?: "-",
                            style = MaterialTheme.typography
                                .bodySmall,
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector =
                            Icons.Outlined.PersonOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme
                            .onSurfaceVariant
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.spacedBy(Spacing.Micro)
                ) {
                    Text(
                        text = "Total pembayaran",
                        style =
                            MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme
                            .onSurfaceVariant
                    )

                    Text(
                        text = (
                                detail.totalAfterDiscount ?: 0L
                                ).toRupiah(),
                        style = MaterialTheme.typography
                            .headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "${
                            detail.items.sumOf {
                                it.quantity ?: 0
                            }
                        } barang",
                        style =
                            MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme
                            .onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                TransactionDetailTable(
                    title = "Rincian transaksi",
                    items = detail.items.map {
                        it.toTransactionTableItem()
                    },
                    subtotalLabel =
                        "Subtotal semua produk",
                    subtotalValue = (
                            detail.totalBeforeDiscount ?: 0L
                            ).toRupiah(),
                    discountLabel = "Total diskon",
                    discountValue = "-${
                        (detail.totalDiscount ?: 0L)
                            .toRupiah()
                    }",
                    showDiscount =
                        (detail.totalDiscount ?: 0L) > 0L,
                    totalLabel = "Total",
                    totalValue = (
                            detail.totalAfterDiscount ?: 0L
                            ).toRupiah()
                )
            }
        }
    }
}