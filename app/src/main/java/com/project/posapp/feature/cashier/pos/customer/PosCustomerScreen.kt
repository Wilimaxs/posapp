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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.project.posapp.feature.cashier.pos.customer.composables.PosCustomerList
import com.project.posapp.feature.cashier.pos.customer.composables.PosCustomerSearchBar
import com.project.posapp.model.PosCustomer
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.composable.PrimaryButton

@Composable
fun PosCustomerScreen(
    currentMember: PosCustomer?,
    onDismiss: () -> Unit,
    onConfirm: (PosCustomer) -> Unit,
    viewModel: PosCustomerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadCustomers(selectedMember = currentMember)
    }

    Dialog(
        onDismissRequest = { viewModel.reset(); onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.40f)),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = Spacing.Large),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Pilih Member",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(Modifier.height(Spacing.Micro))

                        Text(
                            text = "Cari dan pilih member untuk transaksi ini.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = { viewModel.reset(); onDismiss() }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Tutup"
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                PosCustomerSearchBar(
                    value = state.searchQuery,
                    onValueChange = viewModel::onSearchChange,
                    modifier = Modifier.padding(all = Spacing.Large)
                )
                PosCustomerList(
                    state = state,
                    onCustomerClick = viewModel::selectCustomer,
                    onRetry = { viewModel.loadCustomers(selectedMember = currentMember) },
                    onLoadMore = viewModel::loadNextPage,
                    modifier = Modifier.weight(1f)
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = Spacing.Large),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    PrimaryButton(
                        text = "Batal",
                        reverse = true,
                        fillMaxWidth = false,
                        onClick = {
                            viewModel.reset()
                            onDismiss()
                        }
                    )

                    Spacer(Modifier.size(Spacing.Standard))

                    PrimaryButton(
                        text = "Pilih Member",
                        enabled = state.selectedCustomer != null,
                        fillMaxWidth = false,
                        onClick = {
                            val customer = state.selectedCustomer ?: return@PrimaryButton
                            onConfirm(customer)
                            viewModel.reset()
                        }
                    )
                }
            }
        }
    }
}