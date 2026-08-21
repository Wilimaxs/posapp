package com.project.posapp.feature.cashier.history.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Radius
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Success
import com.project.posapp.core.theme.Warning
import com.project.posapp.feature.cashier.history.HistoryUiState
import com.project.posapp.feature.cashier.history.toHistoryCustomerType
import com.project.posapp.feature.cashier.history.toHistoryPaymentMethod
import com.project.posapp.feature.cashier.history.toHistoryPaymentStatus
import com.project.posapp.model.HistoryTransaction
import com.project.posapp.utils.composable.AppBadge
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.extensions.toDisplayTime
import com.project.posapp.utils.extensions.toRupiah
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HistoryTransactionList(
    state: HistoryUiState,
    onTransactionClick: (String) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        listState,
        state.transactions.size,
        state.hasNextPage
    ) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index
        }
            .distinctUntilChanged()
            .collect { lastIndex ->
                if (
                    lastIndex != null &&
                    lastIndex >= state.transactions.lastIndex - 2 &&
                    state.hasNextPage
                ) {
                    onLoadMore()
                }
            }
    }

    LaunchedEffect(
        state.transactions.firstOrNull()?.invoiceNumber
    ) {
        if (state.transactions.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest,
                RoundedCornerShape(Radius.Medium)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(Radius.Medium)
            )
    ) {
        AppState(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Standard),
            isLoading = state.isListLoading,
            errorMessage = state.listErrorMessage,
            isEmpty = state.transactions.isEmpty(),
            errorTitle = "Riwayat gagal dimuat",
            emptyTitle = "Riwayat tidak ditemukan",
            emptyDescription = "Coba ubah pencarian atau filter.",
            emptyIcon = Icons.Outlined.SearchOff,
            onAction = onRetry
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(
                    items = state.transactions,
                    key = {
                        it.saleId
                            ?: it.invoiceNumber
                            ?: it.hashCode()
                    }
                ) { transaction ->
                    HistoryTransactionItem(
                        transaction = transaction,
                        selected =
                            transaction.invoiceNumber ==
                                    state.selectedInvoiceNumber,
                        onClick = {
                            transaction.invoiceNumber?.let(
                                onTransactionClick
                            )
                        }
                    )
                }

                if (state.isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.Standard),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTransactionItem(
    transaction: HistoryTransaction,
    selected: Boolean,
    onClick: () -> Unit
) {
    val isPartial = transaction.paymentStatus.equals(
        other = "partial",
        ignoreCase = true
    )

    val shape = RoundedCornerShape(Radius.Default)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                enabled = transaction.invoiceNumber != null,
                onClick = onClick
            )
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                        .copy(alpha = 0.10f)
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLowest
                }
            )
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(Spacing.Standard),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
            ) {
                Text(
                    text = transaction.invoiceNumber ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = transaction.customer?.name ?: "Guest",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = buildString {
                        append(
                            transaction.customerType
                                .toHistoryCustomerType()
                        )
                        append(" • ")
                        append(
                            transaction.paymentMethod
                                .toHistoryPaymentMethod()
                        )
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(Spacing.Tight))

            Column(
                modifier = Modifier.width(96.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(
                    Spacing.Micro
                )
            ) {
                Text(
                    text = transaction.createdAt.toDisplayTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = (
                            transaction.totalAfterDiscount
                                ?: 0L
                            ).toRupiah(),
                    style = MaterialTheme.typography.titleMedium
                )

                AppBadge(
                    text = transaction.paymentStatus
                        .toHistoryPaymentStatus(),
                    containerColor = if (isPartial) {
                        Warning.copy(alpha = 0.12f)
                    } else {
                        Success.copy(alpha = 0.12f)
                    },
                    contentColor = if (isPartial) {
                        Warning
                    } else {
                        Success
                    }
                )

                if (isPartial) {
                    Text(
                        text = "Sisa ${
                            (transaction.remainingBalance ?: 0L)
                                .toRupiah()
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = Warning
                    )
                }
            }
        }
    }
}