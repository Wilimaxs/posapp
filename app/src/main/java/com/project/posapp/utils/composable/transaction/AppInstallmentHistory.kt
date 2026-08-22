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
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.utils.extensions.toRupiah

data class InstallmentHistoryItem(
    val title: String,
    val amount: Long,
    val metadata: String
)

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
        verticalArrangement =
            Arrangement.spacedBy(Spacing.Standard)
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
                        MaterialTheme.colorScheme.surfaceContainer,
                        RoundedCornerShape(Radius.Default)
                    )
                    .border(
                        width = 1.dp,
                        color =
                            MaterialTheme.colorScheme.outlineVariant,
                        shape =
                            RoundedCornerShape(Radius.Default)
                    )
                    .padding(Spacing.Standard),
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

@Composable
private fun InstallmentRow(
    item: InstallmentHistoryItem
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(Spacing.Standard),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.width(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(
                        MaterialTheme
                            .colorScheme
                            .surfaceContainerLowest,
                        CircleShape
                    )
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
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(Radius.Default)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(Radius.Default)
                )
                .padding(Spacing.Standard),
            verticalArrangement =
                Arrangement.spacedBy(Spacing.Micro)
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