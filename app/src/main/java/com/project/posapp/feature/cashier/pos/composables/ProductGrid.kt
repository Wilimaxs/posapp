package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.posapp.core.theme.Spacing
import com.project.posapp.core.theme.Warning
import com.project.posapp.feature.cashier.pos.CustomerType
import com.project.posapp.feature.cashier.pos.PosUiState
import com.project.posapp.feature.cashier.pos.calculatePricing
import com.project.posapp.model.PosProduct
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.composable.AppBadge
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.toRupiah
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ProductGrid(
    state: PosUiState,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onProductClick: (PosProduct) -> Unit
) {

    val gridState = rememberLazyGridState()

    LaunchedEffect(key1 = gridState, key2 = state.products.size, key3 = state.hasNextPage) {
        snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.distinctUntilChanged().collect { lastVisibleIndex ->
            if (lastVisibleIndex != null && lastVisibleIndex >= state.products.lastIndex - 3 && state.hasNextPage) {
                onLoadMore()
            }
        }
    }

    AppState(
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        isEmpty = state.products.isEmpty(),
        errorTitle = "Produk gagal dimuat",
        emptyTitle = "Produk tidak ditemukan",
        emptyDescription = "Coba gunakan kata kunci atau kategori lain.",
        emptyIcon = Icons.Outlined.SearchOff,
        onAction = onRetry
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 3),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Standard),
            verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
        ) {
            items(
                items = state.products,
                key = PosProduct::id
            ) { product ->
                ProductCard(
                    product = product,
                    quantityInCart = state.cart[product.id]?.quantity ?: 0,
                    customerType = state.customerType,
                    onClick = { onProductClick(product) }
                )
            }

            if (state.isLoadingMore) {
                item(
                    span = {
                        GridItemSpan(currentLineSpan = maxLineSpan)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = Spacing.Standard),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: PosProduct,
    quantityInCart: Int,
    customerType: CustomerType,
    onClick: () -> Unit
) {
    val isOutOfStock = product.stock <= 0
    val isLowStock = product.stock in 1..product.minimumStock
    val pricing = product.calculatePricing(customerType)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isOutOfStock) 0.55f else 1f)
            .background(
                color = if (quantityInCart > 0) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = if (quantityInCart > 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .clickable(
                enabled = !isOutOfStock,
                onClick = onClick
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = Radius.Medium,
                        topEnd = Radius.Medium
                    )
                )
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            if (product.imageUrl.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            AppBadge(
                text = "Stok: ${product.stock}",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(all = Spacing.Tight),
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                contentColor = if (isOutOfStock) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                borderColor = if (isOutOfStock) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                borderWidth = 1.dp
            )
            if (isLowStock) {
                AppBadge(
                    text = "Stok rendah",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(all = Spacing.Tight),
                    containerColor = Warning,
                    contentColor = Color.White
                )
            }
            if (isOutOfStock) {
                AppBadge(
                    text = "HABIS",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .rotate(degrees = -12f),
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    shape = RoundedCornerShape(size = Radius.Default),
                    textStyle = MaterialTheme.typography.labelLarge
                )
            }
        }
        Column(
            modifier = Modifier.padding(all = Spacing.Compact)
        ) {
            if (quantityInCart > 0) {
                Text(
                    text = "$quantityInCart di keranjang",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = product.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = Spacing.Tight)
            )

            if (pricing.hasDiscount) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pricing.basePrice.toRupiah(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = TextDecoration.LineThrough
                    )

                    Text(
                        text = pricing.discountedPrice!!.toRupiah(),
                        modifier = Modifier.padding(start = Spacing.Tight),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Diskon untuk 1 item",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

            } else {
                Text(
                    text = pricing.basePrice.toRupiah(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isOutOfStock) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Text(
                text = when (customerType) {
                    CustomerType.GUEST -> "Grosir: ${product.price.grocier.toRupiah()}"
                    CustomerType.MEMBER -> "Normal: ${product.price.normal.toRupiah()}"
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}