package com.project.posapp.feature.cashier.receivable.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.ReceivableDetail
import com.project.posapp.utils.composable.AppDialog
import com.project.posapp.utils.composable.AppForm
import com.project.posapp.utils.composable.AppResultDialog
import com.project.posapp.utils.composable.AppResultNotification
import com.project.posapp.utils.composable.AppResultType
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.composable.transaction.AppDetailInformation
import com.project.posapp.utils.composable.transaction.AppDetailInformationLayout
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun ReceivablePaymentScreen(
    detail: ReceivableDetail,
    onDismiss: () -> Unit,
    onSuccessFinish: () -> Unit,
    viewModel: ReceivablePaymentViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(detail.saleId) {
        viewModel.initialize(detail)
    }

    when {
        state.payment != null -> {
            AppResultDialog(
                type = AppResultType.SUCCESS,
                title = "Pembayaran Berhasil",
                message = "Pembayaran piutang berhasil diterima.",
                notification = AppResultNotification(
                    label = "Pembayaran diterima",
                    value = state.amountValue.toRupiah()
                ),
                primaryButtonText = "Selesai",
                onPrimaryClick = {
                    viewModel.reset()
                    onSuccessFinish()
                }
            )
        }

        state.errorMessage != null -> {
            AppResultDialog(
                type = AppResultType.ERROR,
                title = "Pembayaran Gagal",
                message = state.errorMessage
                    ?: "Pembayaran piutang gagal diproses.",
                primaryButtonText = "Coba Lagi",
                onPrimaryClick = {
                    viewModel.dismissError()
                },
                secondaryButtonText = "Batal",
                onSecondaryClick = {
                    viewModel.reset()
                    onDismiss()
                }
            )
        }

        else -> {
            ReceivablePaymentDialog(
                state = state,
                onAmountChange = viewModel::onAmountChange,
                onNotesChange = viewModel::onNotesChange,
                onSubmit = viewModel::submit,
                onDismiss = {
                    if (!state.isLoading) {
                        viewModel.reset()
                        onDismiss()
                    }
                }
            )
        }
    }
}

@Composable
private fun ReceivablePaymentDialog(
    state: ReceivablePaymentUiState,
    onAmountChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    AppDialog(
        onDismiss = onDismiss,
        widthFraction = 0.5f,
        maxWidth = 680.dp,
        dismissOnBackPress = !state.isLoading,
        dismissOnClickOutside = !state.isLoading
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AppDetailInformation(
                label = "Terima Pembayaran Piutang",
                labelStyle = MaterialTheme.typography.titleLarge,
                value = "Masukkan nominal pembayaran dan catatan pembayaran.",
                valueStyle = MaterialTheme.typography.bodyMedium,
                valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Large, vertical = Spacing.Standard),
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(state = rememberScrollState())
                    .padding(Spacing.Large),
                verticalArrangement = Arrangement.spacedBy(Spacing.Large),
            ) {
                PaymentCalculationCard(
                    paymentAmount = state.amountValue,
                    remainingBalance = state.remainingAfterPayment
                )

                AppForm(
                    value = state.amount,
                    onValueChange = onAmountChange,
                    label = "Nominal pembayaran",
                    required = true,
                    prefixText = "Rp",
                    placeholder = "Masukkan nominal pembayaran",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    errorMessage = state.amountError,
                    modifier = Modifier.fillMaxWidth()
                )

                AppForm(
                    value = state.notes,
                    onValueChange = onNotesChange,
                    label = "Catatan",
                    labelHelper = "(Opsional)",
                    placeholder = "Contoh: Cicilan kedua",
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            PreviewFooter(
                enabled = state.canSubmit,
                isLoading = state.isLoading,
                onDismiss = onDismiss,
                onContinue = onSubmit
            )
        }
    }
}

@Composable
private fun PaymentCalculationCard(
    paymentAmount: Long,
    remainingBalance: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .padding(all = Spacing.Standard),
        horizontalArrangement = Arrangement.spacedBy(
            Spacing.Standard
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
        ) {
            AppDetailInformation(
                label = "Pembayaran sekarang",
                value = paymentAmount.toRupiah(),
                labelStyle = MaterialTheme.typography.bodyLarge,
                labelColor = MaterialTheme.colorScheme.surface,
                valueStyle = MaterialTheme.typography.titleMedium,
                valueColor = MaterialTheme.colorScheme.surface,
                layout = AppDetailInformationLayout.HORIZONTAL,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            AppDetailInformation(
                label = "Sisa setelah pembayaran",
                value = remainingBalance.toRupiah(),
                labelStyle = MaterialTheme.typography.bodyLarge,
                labelColor = MaterialTheme.colorScheme.surface,
                valueStyle = MaterialTheme.typography.titleMedium,
                valueColor = MaterialTheme.colorScheme.surface,
                layout = AppDetailInformationLayout.HORIZONTAL,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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