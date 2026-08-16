package com.project.posapp.feature.cashier.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.AppBackground
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.composables.CartPanel
import com.project.posapp.feature.cashier.pos.composables.CategorySelector
import com.project.posapp.feature.cashier.pos.composables.CustomerSelector
import com.project.posapp.feature.cashier.pos.composables.ProductGrid
import com.project.posapp.feature.cashier.pos.customer.PosCustomerScreen
import com.project.posapp.feature.cashier.pos.preview.PosPreviewScreen
import com.project.posapp.utils.composable.AppForm

@Composable
fun PosScreen(
    viewModel: PosViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(0.62f)
                .fillMaxSize()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                .padding(all = Spacing.Large)
        ) {
            CustomerSelector(
                customerType = state.customerType,
                selectedMember = state.selectedMember,
                onCustomerTypeChange = viewModel::onCustomerTypeChange,
                onChooseMember = viewModel::showCustomerPicker,
            )
            Spacer(modifier = Modifier.height(Spacing.Large))
            AppForm(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchChange,
                placeholder = "Cari nama produk, SKU, atau barcode",
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                trailingIcon = if (state.searchQuery.isNotEmpty()) {
                    {
                        IconButton(
                            onClick = {
                                viewModel.onSearchChange(query = "")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "Hapus pencarian"
                            )
                        }
                    }
                } else {
                    null
                }
            )
            CategorySelector(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                onCategorySelected = viewModel::onCategorySelected
            )
            ProductGrid(
                state = state,
                onRetry = viewModel::loadProducts,
                onLoadMore = viewModel::loadNextPage,
                onProductClick = viewModel::addProduct
            )
        }
        CartPanel(
            modifier = Modifier.weight(0.38f),
            state = state,
            onIncrease = viewModel::increaseQuantity,
            onDecrease = viewModel::decreaseQuantity,
            onClear = viewModel::clearCart,
            onPay = viewModel::showPaymentPreview
        )
    }

    if (state.showCustomerPicker) {
        PosCustomerScreen(
            currentMember = state.selectedMember,
            onDismiss = viewModel::hideCustomerPicker,
            onConfirm = viewModel::selectMember,
        )
    }

    if (state.showPaymentPreview) {
        PosPreviewScreen(
            customerId = state.selectedMember?.id,
            items = state.cart.mapValues { it.value.quantity },
            onDismiss = viewModel::hidePaymentPreview,
            onPaymentFinished = viewModel::finishPayment
        )
    }
}