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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Success
import com.project.posapp.feature.cashier.pos.customer.PosCustomerUiState
import com.project.posapp.model.PosCustomer
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.toRupiah
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PosCustomerList(
    state: PosCustomerUiState,
    onCustomerClick: (PosCustomer) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = listState, key2 = state.customers.size, key3 = state.hasNextPage) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= state.customers.lastIndex - 2 && state.hasNextPage) {
                    onLoadMore()
                }
            }
    }

    AppState(
        modifier = modifier.fillMaxWidth(),
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        isEmpty = state.customers.isEmpty(),
        errorTitle = "Member gagal dimuat",
        emptyTitle = "Member tidak ditemukan",
        emptyDescription = "Coba gunakan nama atau nomor telepon lain.",
        emptyIcon = Icons.Outlined.SearchOff,
        onAction = onRetry
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Large),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(Spacing.Tight)
        ) {
            items(
                items = state.customers,
                key = { customer ->
                    customer.id ?: customer.customerCode ?: customer.hashCode()
                }
            ) { customer ->
                PosCustomerItem(
                    customer = customer,
                    selected = customer.id != null && state.selectedCustomer?.id == customer.id,
                    onClick = {
                        onCustomerClick(customer)
                    }
                )
            }

            if (state.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = Spacing.Standard),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(Spacing.Standard))
            }
        }
    }
}

@Composable
private fun PosCustomerItem(
    customer: PosCustomer,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(size = Radius.Medium)
    val receivable = customer.receivable

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.10f)
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLowest
                }
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
            .padding(all = Spacing.Standard),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = 2.dp,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.size(Spacing.Standard))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = customer.name ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = customer.phone ?: "-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(
                modifier = Modifier.height(
                    Spacing.Tight
                )
            )

            Text(
                text = customer.address ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(
                    Spacing.Tight
                )
            )

            Text(
                text = if (receivable == null) {
                    "Tidak ada piutang"
                } else {
                    "Total piutang ${receivable.totalRemainingBalance.toRupiah()} " +
                            "dari ${receivable.transactionCount} transaksi"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (receivable == null) {
                    Success
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
    }
}