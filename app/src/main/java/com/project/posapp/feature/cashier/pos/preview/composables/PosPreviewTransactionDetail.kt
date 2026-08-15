package com.project.posapp.feature.cashier.pos.preview.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.PosCheckoutPreview
import com.project.posapp.model.PosCheckoutPreviewItem
import com.project.posapp.ui.theme.Radius
import com.project.posapp.utils.toRupiah

@Composable
fun PosPreviewTransactionDetail(
    preview: PosCheckoutPreview,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .padding(all = Spacing.Standard),
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        Text(
            text = "Rincian transaksi",
            style = MaterialTheme.typography.titleMedium
        )
        TableHeader()
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        preview.items.orEmpty().forEach { item -> PreviewItemRow(item = item) }

        HorizontalDivider(
            modifier = Modifier.padding(top = Spacing.Tight),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        SummaryRow(
            label = "Subtotal semua produk",
            value = (preview.totalBeforeDiscount ?: 0L).toRupiah()
        )

        if ((preview.totalDiscount ?: 0L) > 0) {
            SummaryRow(
                label = "Total diskon",
                value = "-${(preview.totalDiscount ?: 0L).toRupiah()}",
                isDiscount = true
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = Spacing.Micro),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = (preview.totalAfterDiscount ?: 0L).toRupiah(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TableHeader() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Produk",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Harga",
            modifier = Modifier.weight(0.34f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Qty",
            modifier = Modifier.weight(0.16f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Subtotal",
            modifier = Modifier.weight(0.38f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PreviewItemRow(
    item: PosCheckoutPreviewItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Micro)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
        ) {
            Text(
                text = item.name ?: "-",
                style = MaterialTheme.typography.bodyMedium
            )

            item.discount?.let { discount ->
                Text(
                    text = buildString {
                        append(discount.name ?: "Diskon")
                        discount.value?.let { value ->
                            append(" • Diskon ${value.toRupiah()} / item")
                        }
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = (item.unitPrice ?: 0L).toRupiah(),
            modifier = Modifier.weight(0.34f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = (item.quantity ?: 0).toString(),
            modifier = Modifier.weight(0.16f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = (item.subtotalAfterDiscount ?: 0L).toRupiah(),
            modifier = Modifier.weight(0.38f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    isDiscount: Boolean = false
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
            color = if (isDiscount) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}