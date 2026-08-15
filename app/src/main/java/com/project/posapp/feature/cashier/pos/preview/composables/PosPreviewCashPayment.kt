package com.project.posapp.feature.cashier.pos.preview.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.ui.theme.Radius
import com.project.posapp.utils.toRupiah
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PosPreviewCashPayment(
    paymentAmount: Long,
    cashReceived: String,
    onCashReceivedChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cashReceivedAmount = cashReceived.toLongOrNull() ?: 0L

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        Text(
            text = "Pembayaran tunai",
            style = MaterialTheme.typography.titleMedium
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(size = Radius.Medium)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(size = Radius.Medium)
                )
                .padding(all = Spacing.Standard),
            verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total pembayaran",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = paymentAmount.toRupiah(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            OutlinedTextField(
                value = cashReceived.formatAmount(),
                onValueChange = onCashReceivedChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Uang diterima") },
                leadingIcon = {
                    Text(
                        text = "Rp",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(size = Radius.Medium)
            )

            CashQuickAmount(
                paymentAmount = paymentAmount,
                selectedAmount = cashReceivedAmount,
                onSelected = {
                    onCashReceivedChange(it.toString())
                }
            )
        }
    }
}

@Composable
private fun CashQuickAmount(
    paymentAmount: Long,
    selectedAmount: Long,
    onSelected: (Long) -> Unit
) {
    val suggestions = cashSuggestions(paymentAmount = paymentAmount)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        CashSuggestionButton(
            text = "Uang pas",
            selected = selectedAmount == paymentAmount,
            onClick = {
                onSelected(paymentAmount)
            },
            modifier = Modifier.weight(1f)
        )

        suggestions.forEach { amount ->
            CashSuggestionButton(
                text = amount.toRupiah(),
                selected = selectedAmount == amount,
                onClick = {
                    onSelected(amount)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CashSuggestionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(size = Radius.Medium)

    Text(
        text = text,
        modifier = modifier
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = shape
            )
            .border(
                width = if (selected) {
                    2.dp
                } else {
                    1.dp
                },
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(
                vertical = Spacing.Tight,
                horizontal = Spacing.Micro
            ),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium,
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )
}

private fun cashSuggestions(
    paymentAmount: Long
): List<Long> {
    if (paymentAmount <= 0L) {
        return emptyList()
    }

    val first = paymentAmount.roundUp(
        denomination = 10_000L
    )

    val second = paymentAmount.roundUp(
        denomination = 50_000L
    ).let {
        if (it <= first) {
            first + 50_000L
        } else {
            it
        }
    }

    val third = second + 50_000L

    return listOf(
        first,
        second,
        third
    )
}

private fun Long.roundUp(
    denomination: Long
): Long {
    return (
            (this + denomination - 1) /
                    denomination
            ) * denomination
}

private fun String.formatAmount(): String {
    if (isBlank()) {
        return ""
    }

    return toLongOrNull()
        ?.let {
            NumberFormat
                .getNumberInstance(
                    Locale.forLanguageTag("id-ID")
                )
                .format(it)
        }
        ?: ""
}