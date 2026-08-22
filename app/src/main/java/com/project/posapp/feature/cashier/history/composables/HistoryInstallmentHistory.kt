package com.project.posapp.feature.cashier.history.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.posapp.model.HistoryDetail
import com.project.posapp.utils.composable.AppInstallmentHistory
import com.project.posapp.utils.composable.InstallmentHistoryItem
import com.project.posapp.utils.extensions.toLongDisplayDateTime
import com.project.posapp.utils.extensions.toPaymentMethodLabel

@Composable
fun HistoryInstallmentHistory(
    detail: HistoryDetail,
    modifier: Modifier = Modifier
) {
    val payment = detail.payment ?: return

    AppInstallmentHistory(
        modifier = modifier,
        initialPayment = InstallmentHistoryItem(
            title = "Uang Muka (DP)",
            amount = payment.initialPayment ?: 0L,
            metadata = listOfNotNull(
                detail.createdAt.toLongDisplayDateTime(),
                payment.method.toPaymentMethodLabel(),
                detail.user?.name?.let {
                    "Kasir: $it"
                }
            ).joinToString(" • ")
        ),
        installments =
            detail.receivablePayments.mapIndexed { index, item ->
                InstallmentHistoryItem(
                    title = item.notes
                        ?.takeIf(String::isNotBlank)
                        ?: "Pembayaran cicilan ${index + 1}",
                    amount = item.amount ?: 0L,
                    metadata = listOfNotNull(
                        item.createdAt.toLongDisplayDateTime(),
                        item.user?.name?.let {
                            "Kasir: $it"
                        }
                    ).joinToString(" • ")
                )
            }
    )
}