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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.preview.PosPaymentMethod
import com.project.posapp.core.theme.Radius

@Composable
fun PosPreviewPaymentMethode(
    onMethodSelected: (PosPaymentMethod) -> Unit,
    selectedMethod: PosPaymentMethod?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
        ) {
            Text(
                text = "Metode Pembayaran",
                style = MaterialTheme.typography.labelLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Standard)
            ) {
                PaymentMethodCard(
                    title = "Tunai",
                    description = "Bayar menggunakan uang tunai",
                    icon = Icons.Outlined.Payments,
                    selected = selectedMethod == PosPaymentMethod.CASH,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onMethodSelected(PosPaymentMethod.CASH)
                    }
                )

                PaymentMethodCard(
                    title = "QR",
                    description = "Pindai QR untuk melakukan pembayaran",
                    icon = Icons.Outlined.QrCode2,
                    selected = selectedMethod == PosPaymentMethod.QR,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onMethodSelected(PosPaymentMethod.QR)
                    }
                )
            }
        }
        if (selectedMethod == null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing.Standard
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Pilih salah satu metode pembayaran untuk melanjutkan.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }

}

@Composable
private fun PaymentMethodCard(
    title: String,
    description: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(size = Radius.Medium)

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = shape
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(all = Spacing.Standard),
        verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}