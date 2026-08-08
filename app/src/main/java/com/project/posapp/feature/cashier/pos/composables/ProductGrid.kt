package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.Product
import com.project.posapp.utils.composable.ErrorState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ProductGrid(
    products: List<Product>,
    cart: Map<Long, Int>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    hasNextPage: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onProductClick: (Product) -> Unit
) {

    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState, products.size, hasNextPage) {
        snapshotFlow {
            gridState.layoutInfo
                .visibleItemsInfo
                .lastOrNull()
                ?.index
        }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->

                if (
                    lastVisibleIndex != null &&
                    lastVisibleIndex >= products.lastIndex - 3 &&
                    hasNextPage
                ) {
                    onLoadMore()
                }
            }
    }

    when {

        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null -> {
            ErrorState(
                title = "Produk gagal dimuat",
                message = errorMessage,
                onRetry = onRetry
            )
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement =
                    Arrangement.spacedBy(Spacing.Standard),
                verticalArrangement =
                    Arrangement.spacedBy(Spacing.Standard)
            ) {
                items(
                    items = products,
                    key = { it.id }
                ) { product ->

                    ProductCard(
                        product = product,
                        quantityInCart = cart[product.id] ?: 0,
                        onClick = {
                            onProductClick(product)
                        }
                    )
                }

                if (isLoadingMore) {
                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.Standard),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}