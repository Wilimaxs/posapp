package com.project.posapp.feature.cashier.pos.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPaymentMethode
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPaymentOptions
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewSummary
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewTransactionDetail
import com.project.posapp.ui.theme.Radius

@Composable
fun PosPreviewScreen(
    state: PosPreviewUiState,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    onSchemeSelected: (PosPaymentScheme) -> Unit,
    onMethodSelected: (PosPaymentMethod) -> Unit,
    onContinue: (
        saleId: Long,
        scheme: PosPaymentScheme,
        method: PosPaymentMethod
    ) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.scrim
                        .copy(alpha = 0.40f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .widthIn(max = 680.dp)
                    .heightIn(max = 720.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shape = RoundedCornerShape(size = Radius.Medium)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(size = Radius.Medium)
                    )
            ) {
                PreviewHeader(onDismiss = onDismiss)

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                when {
                    state.isLoading -> {
                        LoadingContent()
                    }

                    state.errorMessage != null -> {
                        ErrorContent(
                            message = state.errorMessage,
                            onDismiss = onDismiss,
                            onRetry = onRetry
                        )
                    }

                    state.preview != null -> {
                        val preview = state.preview

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(state = rememberScrollState())
                                .padding(all = Spacing.Large),
                            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
                        ) {
                            PosPreviewSummary(
                                preview = preview,
                                countdownText = state.countdownText
                            )
                            PosPreviewPaymentOptions(
                                isMember = preview.customerType.equals(
                                    other = "member",
                                    ignoreCase = true
                                ),
                                selectedScheme = state.paymentSchema,
                                onSchemeSelected = onSchemeSelected,
                            )
                            PosPreviewTransactionDetail(preview = preview)
                            PosPreviewPaymentMethode(
                                selectedMethod = state.paymentMethod,
                                onMethodSelected = onMethodSelected
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        PreviewFooter(
                            enabled = state.canContinue,
                            onDismiss = onDismiss,
                            onContinue = {
                                val saleId = preview.saleId ?: return@PreviewFooter

                                val method = state.paymentMethod ?: return@PreviewFooter

                                onContinue(
                                    saleId,
                                    state.paymentSchema,
                                    method
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewHeader(
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Spacing.Large,
                vertical = Spacing.Standard
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Pembayaran",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Pilih skema dan metode pembayaran untuk menyelesaikan transaksi.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(
            onClick = onDismiss
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Tutup"
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.ExtraLarge),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
        ) {
            CircularProgressIndicator()
            Text(
                text = "Menyiapkan rincian pembayaran...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        Text(
            text = "Gagal menyiapkan pembayaran",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
        ) {
            TextButton(onClick = onDismiss) {
                Text(text = "Tutup")
            }

            Button(
                onClick = onRetry
            ) {
                Text(text = "Coba lagi")
            }
        }
    }
}

@Composable
private fun PreviewFooter(
    enabled: Boolean,
    onDismiss: () -> Unit,
    onContinue: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Spacing.Large,
                vertical = Spacing.Standard
            ),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onDismiss) {
            Text(text = "Batal")
        }

        Button(
            enabled = enabled,
            onClick = onContinue
        ) {
            Text(text = "Lanjutkan Pembayaran")
        }
    }
}