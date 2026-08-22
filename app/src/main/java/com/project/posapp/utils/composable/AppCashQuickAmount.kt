/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menampilkan pilihan nominal uang tunai secara cepat berdasarkan
 * total pembayaran dan nominal yang sedang dipilih.
 */
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.PosAppTheme
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

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        CashAmountButton(
            text = "Uang Pas",
            selected = selectedAmount == amount,
            onClick = {
                onSelected(amount)
            },
            modifier = Modifier.weight(1f)
        )

        cashSuggestions(amount).forEach { suggestion ->
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

    Text(
        text = text,
        modifier = modifier
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = Spacing.Standard,
                vertical = Spacing.Compact
            ),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
        color = if (selected) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )
}

// Logic Pembuatan daftar pilihan uang tunai
private fun cashSuggestions(
    amount: Long
): List<Long> {
    if (amount <= 0L) return emptyList()

    val first = amount.roundUp(multiple = 10_000L)

    val second = amount
        .roundUp(multiple = 50_000L)
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

@Preview(showBackground = true)
@Composable
private fun AppCashQuickAmountPreview() {
    PosAppTheme {
        AppCashQuickAmount(
            amount = 27_500,
            selectedAmount = 50_000,
            onSelected = {},
            modifier = Modifier.padding(all = Spacing.Standard)
        )
    }
}