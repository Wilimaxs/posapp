package com.project.posapp.feature.cashier.pos.customer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Success
import com.project.posapp.model.PosCustomer
import com.project.posapp.ui.theme.Radius

@Composable
fun PosCustomerItem(
    customer: PosCustomer,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val shape =
        RoundedCornerShape(
            Radius.Medium
        )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                onClick = onClick
            )
            .background(
                if (selected) {
                    MaterialTheme
                        .colorScheme
                        .primaryContainer
                        .copy(alpha = 0.10f)
                } else {
                    MaterialTheme
                        .colorScheme
                        .surfaceContainerLowest
                }
            )
            .border(
                width = 1.dp,

                color = if (selected) {
                    MaterialTheme
                        .colorScheme
                        .primary
                } else {
                    MaterialTheme
                        .colorScheme
                        .outlineVariant
                },

                shape = shape
            )
            .padding(Spacing.Standard),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = 2.dp,

                    color = if (selected) {
                        MaterialTheme
                            .colorScheme
                            .primary
                    } else {
                        MaterialTheme
                            .colorScheme
                            .outlineVariant
                    },

                    shape = CircleShape
                ),

            contentAlignment =
                Alignment.Center
        ) {

            if (selected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            MaterialTheme
                                .colorScheme
                                .primary,
                            CircleShape
                        )
                )
            }
        }

        Spacer(
            modifier =
                Modifier.size(
                    Spacing.Standard
                )
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text = customer.name,
                    style =
                        MaterialTheme
                            .typography
                            .labelLarge,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurface
                )

                Text(
                    text = customer.phone,
                    style =
                        MaterialTheme
                            .typography
                            .labelSmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

            Spacer(
                Modifier.height(
                    Spacing.Micro
                )
            )

            Text(
                text =
                    customer.address
                        ?: "-",

                style =
                    MaterialTheme
                        .typography
                        .labelSmall,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                Modifier.height(
                    Spacing.Micro
                )
            )

            Text(
                text = if (
                    customer.hasReceivable
                ) {
                    "Piutang aktif"
                } else {
                    "Tidak ada piutang"
                },

                style =
                    MaterialTheme
                        .typography
                        .labelSmall,

                color = if (
                    customer.hasReceivable
                ) {
                    MaterialTheme
                        .colorScheme
                        .error
                } else {
                    Success
                }
            )
        }
    }
}