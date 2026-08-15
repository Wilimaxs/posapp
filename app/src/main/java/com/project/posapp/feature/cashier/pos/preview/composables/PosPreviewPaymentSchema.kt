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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.preview.PosPaymentScheme
import com.project.posapp.ui.theme.Radius

@Composable
fun PosPreviewPaymentSchema(
    isMember: Boolean,
    selectedScheme: PosPaymentScheme,
    onSchemeSelected: (PosPaymentScheme) -> Unit,
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
                text = "Skema Pembayaran",
                style = MaterialTheme.typography.labelLarge
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(size = Radius.Medium)
                    )
                    .padding(all = Spacing.Micro),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
            ) {
                SchemeButton(
                    title = "Bayar penuh",
                    selected = selectedScheme == PosPaymentScheme.FULL,
                    enabled = true,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onSchemeSelected(PosPaymentScheme.FULL)
                    }
                )

                SchemeButton(
                    title = "Bayar sebagian",
                    selected = selectedScheme == PosPaymentScheme.PARTIAL,
                    enabled = isMember,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onSchemeSelected(PosPaymentScheme.PARTIAL)
                    }
                )
            }
        }
    }
}

@Composable
private fun SchemeButton(
    title: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(size = Radius.Medium)

    Text(
        text = title,
        modifier = modifier
            .alpha(
                if (enabled) {
                    1f
                } else {
                    0.4f
                }
            )
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                },
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
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(
                vertical = Spacing.Tight,
                horizontal = Spacing.Standard
            ),
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}