package com.project.posapp.feature.cashier.pos.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.customer.composables.PosCustomerList
import com.project.posapp.model.PosCustomer
import com.project.posapp.utils.composable.AppDialog
import com.project.posapp.utils.composable.AppForm
import com.project.posapp.utils.composable.PrimaryButton

@Composable
fun PosCustomerScreen(
    currentMember: PosCustomer?,
    onDismiss: () -> Unit,
    onConfirm: (PosCustomer) -> Unit,
    viewModel: PosCustomerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        viewModel.loadCustomers(selectedMember = currentMember)
    }

    AppDialog(
        onDismiss = {
            viewModel.reset()
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(Modifier.height(Spacing.Micro))

                    Text(
                        text = "Cari dan pilih member untuk transaksi ini.",
                        style = MaterialTheme.typography.bodyMedium,
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

            AppForm(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchChange,
                placeholder = "Cari nama atau nomor telepon",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.onSearchChange(query = "")
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Hapus pencarian"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
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