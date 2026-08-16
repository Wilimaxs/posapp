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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.preview.composables.PosPaymentSuccess
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewCashPayment
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPartialDetail
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPaymentMethode
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPaymentSchema
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewSummary
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewTransactionDetail
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.composable.AppStateDeleted
import com.project.posapp.utils.composable.AppStateTypeDeleted
import com.project.posapp.utils.composable.PrimaryButton

@Composable
fun PosPreviewScreen(
    customerId: Long?,
    items: Map<Long, Int>,
    onDismiss: () -> Unit,
    onPaymentFinished: () -> Unit,
    viewModel: PosPreviewViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadPreview(
            customerId = customerId,
            items = items
        )
    }

    Dialog(
        onDismissRequest = {
            viewModel.dismiss()
            onDismiss()
        },
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
                    .fillMaxWidth(
                        fraction = if (state.payment != null) {
                            0.6f
                        } else {
                            0.5f
                        }
                    )
                    .widthIn(
                        max = if (state.payment != null) {
                            800.dp
                        } else {
                            680.dp
                        }
                    )
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
                PreviewHeader()

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                when {

                    state.isPaymentLoading -> {
                        AppStateDeleted(
                            type = AppStateTypeDeleted.LOADING,
                            title = "Memproses pembayaran...",
                            description = "Mohon tunggu sebentar."
                        )
                    }

                    state.payment != null -> {
                        val payment = requireNotNull(state.payment)
                        PosPaymentSuccess(
                            payment = payment,
                            onFinish = {
                                viewModel.dismiss()
                                onPaymentFinished()
                            }
                        )
                    }

                    state.isLoading -> {
                        AppStateDeleted(
                            type = AppStateTypeDeleted.LOADING,
                            title = "Menyiapkan rincian pembayaran...",
                            description = "Mohon tunggu sebentar."
                        )
                    }

                    state.errorMessage != null -> {
                        AppStateDeleted(
                            type = AppStateTypeDeleted.ERROR,
                            title = "Gagal menyiapkan pembayaran",
                            description = state.errorMessage,
                            onAction = {
                                viewModel.loadPreview(
                                    customerId = customerId,
                                    items = items
                                )
                            },
                            secondaryActionText = "Batal",
                            onSecondaryAction = {
                                viewModel.dismiss()
                                onDismiss()
                            }
                        )
                    }

                    state.preview != null -> {
                        val preview = requireNotNull(state.preview)

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
                            PosPreviewPaymentSchema(
                                isMember = preview.customerType.equals(
                                    other = "member",
                                    ignoreCase = true
                                ),
                                selectedScheme = state.paymentSchema,
                                onSchemeSelected = viewModel::selectedPaymentSchema
                            )
                            PosPreviewTransactionDetail(preview = preview)
                            if (state.paymentSchema == PosPaymentScheme.PARTIAL) {
                                PosPreviewPartialDetail(
                                    total = preview.totalAfterDiscount ?: 0L,
                                    downPayment = state.downPayment,
                                    dueDate = state.dueDate,
                                    remainingReceivable = state.remainingReceivable,
                                    onDownPaymentChange = viewModel::onDownPaymentChange,
                                    onDueDateChange = viewModel::onDueDateChange
                                )
                            }
                            PosPreviewPaymentMethode(
                                selectedMethod = state.paymentMethod,
                                onMethodSelected = viewModel::selectedPaymentMethode
                            )
                            if (state.paymentSchema == PosPaymentScheme.FULL && state.paymentMethod == PosPaymentMethod.CASH) {
                                PosPreviewCashPayment(
                                    paymentAmount = state.paymentAmount,
                                    cashReceived = state.cashReceived,
                                    onCashReceivedChange = viewModel::onCashReceivedChange
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        PreviewFooter(
                            enabled = state.canContinue,
                            onDismiss = {
                                viewModel.dismiss()
                                onDismiss()
                            },
                            onContinue = {
                                viewModel.payment(
                                    onError = {
                                        viewModel.dismiss()
                                        onDismiss()
                                    }
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
private fun PreviewHeader() {
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
        PrimaryButton(
            text = "Batal",
            reverse = true,
            fillMaxWidth = false,
            onClick = onDismiss
        )
        PrimaryButton(
            text = "Lanjutkan Pembayaran",
            enabled = enabled,
            fillMaxWidth = false,
            onClick = onContinue
        )
    }
}