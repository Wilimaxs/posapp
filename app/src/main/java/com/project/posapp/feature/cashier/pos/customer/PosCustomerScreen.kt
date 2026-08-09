package com.project.posapp.feature.cashier.pos.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.customer.composables.PosCustomerItem
import com.project.posapp.feature.cashier.pos.customer.composables.PosCustomerSearchBar
import com.project.posapp.model.PosCustomer
import com.project.posapp.ui.theme.Radius
import com.project.posapp.utils.composable.EmptyState
import com.project.posapp.utils.composable.ErrorState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PosCustomerScreen(
    currentMember: PosCustomer?,
    onDismiss: () -> Unit,
    onConfirm: (PosCustomer) -> Unit,
    viewModel: PosCustomerViewModel =
        hiltViewModel()
) {
    val state by
    viewModel.uiState.collectAsState()

    val listState =
        rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.loadCustomers(
            selectedMember =
                currentMember
        )
    }

    // Pagination
    LaunchedEffect(
        listState,
        state.customers.size,
        state.hasNextPage
    ) {

        snapshotFlow {
            listState
                .layoutInfo
                .visibleItemsInfo
                .lastOrNull()
                ?.index
        }
            .distinctUntilChanged()
            .collect { lastIndex ->

                if (
                    lastIndex != null &&
                    lastIndex >=
                    state.customers.lastIndex - 2 &&
                    state.hasNextPage
                ) {
                    viewModel.loadNextPage()
                }
            }
    }

    Dialog(
        onDismissRequest = {

            viewModel.reset()
            onDismiss()
        },

        properties =
            DialogProperties(
                usePlatformDefaultWidth =
                    false
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme
                        .colorScheme
                        .scrim
                        .copy(alpha = 0.40f)
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .widthIn(
                        max = 680.dp
                    )
                    .heightIn(
                        max = 720.dp
                    )
                    .background(
                        color =
                            MaterialTheme
                                .colorScheme
                                .surfaceContainerLowest,

                        shape =
                            RoundedCornerShape(
                                Radius.Medium
                            )
                    )
                    .border(
                        width = 1.dp,

                        color =
                            MaterialTheme
                                .colorScheme
                                .outlineVariant,

                        shape =
                            RoundedCornerShape(
                                Radius.Medium
                            )
                    )
            ) {

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            Spacing.Large
                        ),

                    horizontalArrangement =
                        Arrangement
                            .SpaceBetween,

                    verticalAlignment =
                        Alignment.Top
                ) {

                    Column {

                        Text(
                            text =
                                "Pilih Member",

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineMedium
                        )

                        Spacer(
                            Modifier.height(
                                Spacing.Micro
                            )
                        )

                        Text(
                            text =
                                "Cari dan pilih member untuk transaksi ini.",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = {

                            viewModel.reset()
                            onDismiss()
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.Close,

                            contentDescription =
                                "Tutup"
                        )
                    }
                }

                HorizontalDivider(
                    color =
                        MaterialTheme
                            .colorScheme
                            .outlineVariant
                )

                // Search
                PosCustomerSearchBar(
                    value =
                        state.searchQuery,

                    onValueChange =
                        viewModel::onSearchChange,

                    modifier = Modifier
                        .padding(
                            Spacing.Large
                        )
                )

                // Content
                when {

                    state.isLoading -> {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }
                    }

                    state.errorMessage != null -> {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            ErrorState(
                                title =
                                    "Member gagal dimuat",

                                message =
                                    state.errorMessage!!,

                                onRetry = {
                                    viewModel
                                        .loadCustomers(
                                            selectedMember =
                                                currentMember
                                        )
                                }
                            )
                        }
                    }

                    state.customers.isEmpty() -> {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            EmptyState(
                                icon =
                                    Icons.Outlined
                                        .SearchOff,

                                title =
                                    "Member tidak ditemukan",

                                description =
                                    "Coba gunakan nama atau nomor telepon lain."
                            )
                        }
                    }

                    else -> {

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(
                                    horizontal =
                                        Spacing.Large
                                ),

                            state = listState,

                            verticalArrangement =
                                Arrangement.spacedBy(
                                    Spacing.Tight
                                )
                        ) {

                            items(
                                items =
                                    state.customers,

                                key = {
                                    it.id
                                }
                            ) { customer ->

                                PosCustomerItem(
                                    customer =
                                        customer,

                                    selected =
                                        state
                                            .selectedCustomer
                                            ?.id ==
                                                customer.id,

                                    onClick = {
                                        viewModel
                                            .selectCustomer(
                                                customer
                                            )
                                    }
                                )
                            }

                            if (
                                state.isLoadingMore
                            ) {

                                item {

                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    Spacing.Standard
                                                ),

                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        CircularProgressIndicator(
                                            modifier =
                                                Modifier.size(
                                                    24.dp
                                                )
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(
                                    Modifier.height(
                                        Spacing.Standard
                                    )
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    color =
                        MaterialTheme
                            .colorScheme
                            .outlineVariant
                )

                // Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            Spacing.Large
                        ),

                    horizontalArrangement =
                        Arrangement.End,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    OutlinedButton(
                        onClick = {

                            viewModel.reset()
                            onDismiss()
                        }
                    ) {
                        Text("Batal")
                    }

                    Spacer(
                        Modifier.size(
                            Spacing.Standard
                        )
                    )

                    Button(
                        enabled =
                            state.selectedCustomer
                                    != null,

                        onClick = {

                            val customer =
                                state.selectedCustomer
                                    ?: return@Button

                            onConfirm(customer)

                            viewModel.reset()
                        }
                    ) {

                        Text(
                            "Pilih Member"
                        )
                    }
                }
            }
        }
    }
}