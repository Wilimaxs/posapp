package com.project.posapp.utils.composable

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
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing

data class TransactionTableColumn(
    val title: String,
    val weight: Float,
    val textAlign: TextAlign = TextAlign.Start
)

data class TransactionTableItem(
    val name: String,
    val price: String,
    val quantity: String,
    val subtotal: String,
    val subLabel: String? = null
)

@Composable
fun TransactionDetailTable(
    title: String,
    items: List<TransactionTableItem>,
    modifier: Modifier = Modifier,
    columns: List<TransactionTableColumn> = defaultTransactionColumns,
    subtotalLabel: String? = null,
    subtotalValue: String? = null,
    discountLabel: String? = null,
    discountValue: String? = null,
    showDiscount: Boolean = false,
    totalLabel: String? = null,
    totalValue: String? = null,
    totalValueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
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
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        TransactionTableHeader(columns = columns)

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        items.forEach { item ->
            TransactionTableItemRow(
                item = item,
                columns = columns
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = Spacing.Tight),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (subtotalLabel != null && subtotalValue != null) {
            TransactionSummaryRow(
                label = subtotalLabel,
                value = subtotalValue,
                valueColor = MaterialTheme.colorScheme.onSurface
            )
        }

        if (showDiscount && discountLabel != null && discountValue != null) {
            TransactionSummaryRow(
                label = discountLabel,
                value = discountValue,
                valueColor = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = Spacing.Micro),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (totalLabel != null && totalValue != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = totalLabel,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = totalValue,
                    style = MaterialTheme.typography.headlineSmall,
                    color = totalValueColor
                )
            }
        }
    }
}

@Composable
private fun TransactionTableHeader(
    columns: List<TransactionTableColumn>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Tight)
    ) {
        columns.forEach { column ->
            Text(
                text = column.title,
                modifier = Modifier.weight(column.weight),
                textAlign = column.textAlign,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TransactionTableItemRow(
    item: TransactionTableItem,
    columns: List<TransactionTableColumn>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Tight)
    ) {
        Column(
            modifier = Modifier.weight(columns[0].weight),
            verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall
            )

            item.subLabel?.let { subLabel ->
                Text(
                    text = subLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = item.price,
            modifier = Modifier.weight(columns[1].weight),
            textAlign = columns[1].textAlign,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = item.quantity,
            modifier = Modifier.weight(columns[2].weight),
            textAlign = columns[2].textAlign,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = item.subtotal,
            modifier = Modifier.weight(columns[3].weight),
            textAlign = columns[3].textAlign,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun TransactionSummaryRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color
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
            color = valueColor
        )
    }
}

private val defaultTransactionColumns = listOf(
    TransactionTableColumn(
        title = "Produk",
        weight = 1f,
        textAlign = TextAlign.Start
    ),
    TransactionTableColumn(
        title = "Harga",
        weight = 0.34f,
        textAlign = TextAlign.End
    ),
    TransactionTableColumn(
        title = "Qty",
        weight = 0.16f,
        textAlign = TextAlign.End
    ),
    TransactionTableColumn(
        title = "Subtotal",
        weight = 0.38f,
        textAlign = TextAlign.End
    )
)