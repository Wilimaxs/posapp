package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.CartItem
import com.project.posapp.feature.cashier.pos.CustomerType
import com.project.posapp.feature.cashier.pos.PosUiState
import com.project.posapp.feature.cashier.pos.calculatePricing
import com.project.posapp.model.PosProduct
import com.project.posapp.core.theme.Radius
import com.project.posapp.utils.composable.AppState
import com.project.posapp.utils.composable.PrimaryButton
import com.project.posapp.utils.extensions.toRupiah

@Composable
fun CartPanel(
    state: PosUiState,
    onIncrease: (PosProduct) -> Unit,
    onDecrease: (PosProduct) -> Unit,
    onClear: () -> Unit,
    onPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = Spacing.Large),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Keranjang (${state.cartCount})",
                style = MaterialTheme.typography.titleLarge
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        AppState(
            isLoading = false,
            errorMessage = null,
            isEmpty = state.cart.isEmpty(),
            modifier = Modifier.weight(1f),
            emptyTitle = "Keranjang masih kosong",
            emptyDescription = "Pilih produk untuk menambahkannya ke keranjang.",
            emptyIcon = Icons.Outlined.ShoppingCart
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(all = Spacing.Standard),
                verticalArrangement = Arrangement.spacedBy(Spacing.Standard)
            ) {
                items(
                    items = state.cartItems,
                    key = { it.product.id }
                ) { item ->
                    CartItemCard(
                        item = item,
                        customerType = state.customerType,
                        onIncrease = { onIncrease(item.product) },
                        onDecrease = { onDecrease(item.product) }
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Column(
            modifier = Modifier.padding(all = Spacing.Standard)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = state.total.toRupiah(),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            PrimaryButton(
                text = "Bayar ${state.total.toRupiah()}",
                onClick = onPay,
                enabled = state.cartItems.isNotEmpty() &&
                        if (state.customerType == CustomerType.MEMBER) {
                            state.selectedMember != null
                        } else {
                            true
                        },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Standard)
            )

            PrimaryButton(
                text = "Kosongkan Keranjang",
                onClick = onClear,
                enabled = state.cartItems.isNotEmpty(),
                reverse = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Tight)
            )
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    customerType: CustomerType,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val product = item.product
    val pricing = product.calculatePricing(
        customerType = customerType,
        quantity = item.quantity
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .border(
                width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(size = Radius.Medium)
            )
            .padding(all = Spacing.Compact)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = pricing.total.toRupiah(),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            text = when (customerType) {
                CustomerType.GUEST -> "Harga normal"
                CustomerType.MEMBER -> "Harga grosir"
            },
            modifier = Modifier.padding(top = Spacing.Tight),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.Compact),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pricing.hasDiscount) {
                Column {
                    Text(
                        text = "1 item diskon: ${pricing.discountedPrice!!.toRupiah()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (item.quantity > 1) {
                        Text(
                            text = "${item.quantity - 1} item lainnya: ${pricing.basePrice.toRupiah()} / item",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Text(
                    text = "${pricing.basePrice.toRupiah()} / item",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onDecrease
                ) {
                    Icon(
                        imageVector = if (item.quantity == 1) {
                            Icons.Outlined.Delete
                        } else {
                            Icons.Outlined.Remove
                        },
                        contentDescription = "Kurangi"
                    )
                }
                Text(
                    text = item.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = onIncrease,
                    enabled = item.quantity < product.stock
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Tambah"
                    )
                }
            }
        }
    }
}