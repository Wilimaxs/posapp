package com.project.posapp.feature.cashier.receivable.composables

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
import com.project.posapp.feature.cashier.receivable.ReceivableUiState
import com.project.posapp.feature.cashier.receivable.toReceivableDueStatus
import com.project.posapp.model.Receivable
import com.project.posapp.utils.composable.AppBadge
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.extensions.toLongDisplayDate
import com.project.posapp.utils.extensions.toRupiah
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ReceivableList(
    state: ReceivableUiState,
    onReceivableClick: (Long) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        listState,
        state.receivables.size,
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
                    lastIndex >=
                    state.receivables.lastIndex - 2 &&
                    state.hasNextPage
                ) {
                    onLoadMore()
                }
            }
    }

    LaunchedEffect(
        state.receivables.firstOrNull()?.saleId
    ) {
        if (state.receivables.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme
                    .surfaceContainerLowest,
                RoundedCornerShape(Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Radius.Medium)
            )
    ) {
        AppState(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Standard),
            isLoading = state.isListLoading,
            errorMessage = state.listErrorMessage,
            isEmpty = state.receivables.isEmpty(),
            errorTitle = "Piutang gagal dimuat",
            emptyTitle = "Piutang tidak ditemukan",
            emptyDescription =
                "Coba ubah pencarian atau filter.",
            emptyIcon = Icons.Outlined.SearchOff,
            onAction = onRetry
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(
                    items = state.receivables,
                    key = {
                        it.saleId ?: it.hashCode()
                    }
                ) { receivable ->
                    ReceivableListItem(
                        receivable = receivable,
                        selected =
                            receivable.saleId ==
                                    state.selectedSaleId,
                        onClick = {
                            receivable.saleId?.let(
                                onReceivableClick
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
private fun ReceivableListItem(
    receivable: Receivable,
    selected: Boolean,
    onClick: () -> Unit
) {
    val accentColor =
        receivable.dueStatus.receivableDueStatusColor()

    val shape = RoundedCornerShape(Radius.Default)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                enabled = receivable.saleId != null,
                onClick = onClick
            )
            .background(
                if (selected) {
                    MaterialTheme.colorScheme
                        .primaryContainer
                        .copy(alpha = 0.10f)
                } else {
                    MaterialTheme.colorScheme
                        .surfaceContainerLowest
                }
            )
            .padding(Spacing.Standard),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(Spacing.Micro)
        ) {
            Text(
                text = receivable.customer?.name ?: "-",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = receivable.invoiceNumber ?: "-",
                style = MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Jatuh tempo ${
                    receivable.dueDate.toLongDisplayDate()
                }",
                style = MaterialTheme.typography.bodySmall,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(
            modifier = Modifier.width(Spacing.Tight)
        )

        Column(
            modifier = Modifier.width(116.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement =
                Arrangement.spacedBy(Spacing.Micro)
        ) {
            AppBadge(
                text = receivable.dueStatus
                    .toReceivableDueStatus(),
                containerColor =
                    accentColor.copy(alpha = 0.12f),
                contentColor = accentColor
            )

            Text(
                text = "Sisa Piutang",
                style = MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = (
                        receivable.remainingBalance ?: 0L
                        ).toRupiah(),
                style = MaterialTheme.typography.titleMedium,
                color = accentColor
            )
        }
    }
}