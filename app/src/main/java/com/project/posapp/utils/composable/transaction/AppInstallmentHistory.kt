/*
 * Dibuat oleh: Wilimaxs
 * Dibuat pada: 22 Agustus 2026
 * Tujuan:
 * Menampilkan riwayat pembayaran berupa uang muka dan daftar cicilan.
 * Digunakan bersama oleh fitur History dan Piutang.
 */
package com.project.posapp.utils.composable.transaction

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.utils.extensions.toRupiah

data class InstallmentHistoryItem(
    val title: String,
    val amount: Long,
    val metadata: String
)

// Reusable Utama
@Composable
fun AppInstallmentHistory(
    initialPayment: InstallmentHistoryItem,
    installments: List<InstallmentHistoryItem>,
    modifier: Modifier = Modifier,
    title: String = "Riwayat Pembayaran Cicilan",
    emptyMessage: String = "Belum ada pembayaran cicilan."
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        InstallmentRow(item = initialPayment)

        installments.forEach { item ->
            InstallmentRow(item = item)
        }

        if (installments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(size = Radius.Default)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(size = Radius.Default)
                    )
                    .padding(all = Spacing.Standard),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Private Pembuatan komponen
@Composable
private fun InstallmentRow(
    item: InstallmentHistoryItem
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Standard),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.width(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )

            VerticalDivider(
                modifier = Modifier.height(56.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(size = Radius.Default)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(size = Radius.Default)
                )
                .padding(all = Spacing.Standard),
            verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = item.amount.toRupiah(),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Text(
                text = item.metadata,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(
    name = "App Installment History",
    showBackground = true,
    widthDp = 600
)
@Composable
private fun AppInstallmentHistoryPreview() {
    AppInstallmentHistory(
        initialPayment = InstallmentHistoryItem(
            title = "Uang Muka (DP)",
            amount = 150_000,
            metadata = "5 Agustus 2026, 14:15 • Tunai • Kasir: Dian Pratama"
        ),
        installments = listOf(
            InstallmentHistoryItem(
                title = "Cicilan kedua",
                amount = 50_000,
                metadata = "10 Agustus 2026, 09:30 • Kasir: Dian Pratama"
            )
        ),
        modifier = Modifier.padding(all = 16.dp)
    )
}