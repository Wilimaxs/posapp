package com.project.posapp.feature.cashier.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.AppBackground
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.composables.CartPanel
import com.project.posapp.feature.cashier.pos.composables.CategorySelector
import com.project.posapp.feature.cashier.pos.composables.CustomerSelector
import com.project.posapp.feature.cashier.pos.composables.ProductGrid
import com.project.posapp.feature.cashier.pos.composables.ProductSearchBar

@Composable
fun PosScreen(
    viewModel: PosViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        Column(
            modifier = Modifier
                .weight(0.62f)
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                .padding(Spacing.Large)
        ) {

            CustomerSelector()

            ProductSearchBar(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchChange
            )

            CategorySelector(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                onCategorySelected = viewModel::onCategorySelected
            )

            ProductGrid(
                products = state.products,
                cart = state.cart,

                isLoading = state.isLoading,
                isLoadingMore = state.isLoadingMore,
                hasNextPage = state.hasNextPage,

                errorMessage = state.errorMessage,

                onRetry = viewModel::loadProducts,
                onLoadMore = viewModel::loadNextPage,
                onProductClick = viewModel::addProduct
            )
        }

        CartPanel(
            modifier = Modifier.weight(0.38f),
            items = state.cartItems,
            cartCount = state.cartCount,
            total = state.total,
            onIncrease = viewModel::increaseQuantity,
            onDecrease = viewModel::decreaseQuantity,
            onClear = viewModel::clearCart
        )
    }
}