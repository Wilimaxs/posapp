package com.project.posapp.utils.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.utils.extensions.roundUp
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun AppCashQuickAmount(
    amount: Long,
    selectedAmount: Long,
    onSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val suggestions = cashSuggestions(amount)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        CashAmountButton(
            text = "Uang pas",
            selected = selectedAmount == amount,
            onClick = {
                onSelected(amount)
            },
            modifier = Modifier.weight(1f)
        )

        suggestions.forEach { suggestion ->
            CashAmountButton(
                text = suggestion.toRupiah(),
                selected = selectedAmount == suggestion,
                onClick = {
                    onSelected(suggestion)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CashAmountButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(Radius.Medium)

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
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = Spacing.Standard,
                vertical = Spacing.Compact
            ),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )
}

private fun cashSuggestions(
    amount: Long
): List<Long> {
    if (amount <= 0L) return emptyList()

    val first = amount.roundUp(10_000L)

    val second = amount
        .roundUp(50_000L)
        .let {
            if (it <= first) {
                first + 50_000L
            } else {
                it
            }
        }

    return listOf(
        first,
        second,
        second + 50_000L
    )
}