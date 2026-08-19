package com.project.posapp.feature.cashier.pos.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewCashPayment
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPartialDetail
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPaymentMethode
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewSummary
import com.project.posapp.utils.composable.AppDialog
import com.project.posapp.utils.composable.AppSegmentedButton
import com.project.posapp.utils.composable.AppSegmentedOption
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.composable.TransactionDetailTable
import com.project.posapp.utils.composable.TransactionTableItem
import com.project.posapp.utils.toRupiah

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
    AppDialog(
        onDismiss = {
            viewModel.dismiss()
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PreviewHeader()

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            AppState(
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading,
                loadingTitle = "Menyiapkan rincian pembayaran...",
                loadingDescription = "Mohon tunggu sebentar.",
                errorMessage = state.errorMessage,
                isEmpty = state.preview == null,
                errorTitle = "Gagal menyiapkan pembayaran",
                onAction = {
                    viewModel.loadPreview(
                        customerId = customerId,
                        items = items
                    )
                },
                onSecondaryAction = {
                    viewModel.dismiss()
                    onDismiss()
                }
            ) {
                val preview = requireNotNull(state.preview)

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
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
                        Text(
                            text = "Skema Pembayaran",
                            style = MaterialTheme.typography.titleMedium
                        )
                        AppSegmentedButton(
                            modifier = Modifier.fillMaxWidth(),
                            options = listOf(
                                AppSegmentedOption(
                                    value = PosPaymentScheme.FULL,
                                    text = "Bayar penuh"
                                ),
                                AppSegmentedOption(
                                    value = PosPaymentScheme.PARTIAL,
                                    text = "Bayar sebagian",
                                    enabled = preview.customerType.equals(
                                        other = "member",
                                        ignoreCase = true
                                    )
                                )
                            ),
                            selected = state.paymentSchema,
                            onSelected = viewModel::selectedPaymentSchema
                        )
                        TransactionDetailTable(
                            title = "Rincian transaksi",
                            items = preview.items.orEmpty().map { item ->
                                TransactionTableItem(
                                    name = item.name ?: "-",
                                    price = (item.unitPrice ?: 0L).toRupiah(),
                                    quantity = (item.quantity ?: 0).toString(),
                                    subtotal = (item.subtotalAfterDiscount ?: 0L).toRupiah(),
                                    subLabel = item.discount?.let { discount ->
                                        buildString {
                                            append(discount.name ?: "Diskon")
                                            discount.value?.let { value ->
                                                append(" • Diskon ${value.toRupiah()} / item")
                                            }
                                        }
                                    }
                                )
                            },
                            subtotalLabel = "Subtotal semua produk",
                            subtotalValue = (preview.totalBeforeDiscount ?: 0L).toRupiah(),
                            discountLabel = "Total diskon",
                            discountValue = "-${(preview.totalDiscount ?: 0L).toRupiah()}",
                            showDiscount = (preview.totalDiscount ?: 0L) > 0,
                            totalLabel = "Total",
                            totalValue = (preview.totalAfterDiscount ?: 0L).toRupiah()
                        )
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
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "Pilih skema dan metode pembayaran untuk menyelesaikan transaksi.",
                style = MaterialTheme.typography.bodyLarge,
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
        Spacer(modifier = Modifier.width(Spacing.Standard))
        PrimaryButton(
            text = "Lanjutkan Pembayaran",
            enabled = enabled,
            fillMaxWidth = false,
            onClick = onContinue
        )
    }
}