package com.project.posapp.feature.cashier.pos.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.preview.composables.PosPaymentDialog
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPartialDetail
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewPaymentMethode
import com.project.posapp.feature.cashier.pos.preview.composables.PosPreviewSummary
import com.project.posapp.utils.composable.AppCashQuickAmount
import com.project.posapp.utils.composable.AppConfirmationDialog
import com.project.posapp.utils.composable.AppDialog
import com.project.posapp.utils.composable.AppForm
import com.project.posapp.utils.composable.AppResultDialog
import com.project.posapp.utils.composable.AppResultType
import com.project.posapp.utils.composable.AppSegmentedButton
import com.project.posapp.utils.composable.AppSegmentedOption
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.composable.TransactionDetailTable
import com.project.posapp.utils.composable.TransactionTableItem
import com.project.posapp.utils.extensions.formatAmount
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun PosPreviewScreen(
    customerId: Long?,
    items: Map<Long, Int>,
    onDismiss: () -> Unit,
    onTransactionReset: () -> Unit,
    viewModel: PosPreviewViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val canRequestCancel =
        !state.isLoading &&
                !state.isPaymentLoading &&
                !state.isCancelLoading

    LaunchedEffect(key1 = Unit) {
        viewModel.loadPreview(
            customerId = customerId,
            items = items
        )
    }

    LaunchedEffect(state.cancelSuccess) {
        if (state.cancelSuccess) {
            val resetTransaction = state.resetTransactionAfterCancel

            viewModel.dismiss()

            if (resetTransaction) {
                onTransactionReset()
            } else {
                onDismiss()
            }
        }
    }

    state.payment?.let { payment ->
        PosPaymentDialog(
            payment = payment,
            onPrintReceipt = {},
            onSendReceipt = {},
            onFinish = {
                viewModel.dismiss()
                onTransactionReset()
            }
        )
        return
    }

    if (state.showCancelConfirmation) {
        AppConfirmationDialog(
            title = "Batalkan Pembayaran?",
            message = "Rincian pembayaran akan dibatalkan dan stok akan dikembalikan.",
            confirmButtonText = "Ya, Batalkan",
            dismissButtonText = "Kembali",
            onConfirm = viewModel::cancelPreview,
            onDismiss = viewModel::dismissCancelConfirmation,
            isLoading = state.isCancelLoading,
            isDestructive = true,
            errorMessage = state.cancelErrorMessage
        )
        return
    }

    state.paymentErrorMessage?.let { message ->
        AppResultDialog(
            type = AppResultType.ERROR,
            title = "Pembayaran Gagal",
            message = message,
            primaryButtonText = "Coba Lagi",
            onPrimaryClick = viewModel::payment,
            secondaryButtonText = "Batal",
            onSecondaryClick = {
                viewModel.requestCancelPreview(
                    resetTransaction = true
                )
            },
            isLoading = state.isPaymentLoading
        )
        return
    }
    AppDialog(
        onDismiss = {
            if (canRequestCancel) {
                viewModel.requestCancelPreview()
            }
        },
        dismissOnBackPress = canRequestCancel,
        dismissOnClickOutside = canRequestCancel
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
                            CashPaymentOption(
                                paymentAmount = state.paymentAmount,
                                cashReceived = state.cashReceived,
                                onCashReceivedChange = viewModel::onCashReceivedChange
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    PreviewFooter(
                        enabled = state.canContinue,
                        isLoading = state.isPaymentLoading,
                        onDismiss = viewModel::requestCancelPreview,
                        onContinue = viewModel::payment
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
    isLoading: Boolean,
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
            enabled = !isLoading,
            fillMaxWidth = false,
            onClick = onDismiss
        )

        Spacer(
            modifier = Modifier.width(Spacing.Standard)
        )

        PrimaryButton(
            text = "Lanjutkan Pembayaran",
            enabled = enabled,
            isLoading = isLoading,
            fillMaxWidth = false,
            onClick = onContinue
        )
    }
}

@Composable
private fun CashPaymentOption(
    paymentAmount: Long,
    cashReceived: String,
    onCashReceivedChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cashReceivedAmount = cashReceived.toLongOrNull() ?: 0L

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
    ) {
        Text(
            text = "Pembayaran Tunai",
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(Radius.Medium)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(Radius.Medium)
                )
                .padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total pembayaran",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = paymentAmount.toRupiah(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            AppForm(
                value = cashReceived.formatAmount(),
                onValueChange = onCashReceivedChange,
                label = "Uang diterima",
                prefixText = "Rp",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                minHeight = 64.dp
            )

            AppCashQuickAmount(
                amount = paymentAmount,
                selectedAmount = cashReceivedAmount,
                onSelected = {
                    onCashReceivedChange(it.toString())
                }
            )
        }
    }
}