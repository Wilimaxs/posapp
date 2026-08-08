package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.pos.CartItem
import com.project.posapp.feature.cashier.pos.CustomerType
import com.project.posapp.model.Product
import com.project.posapp.utils.composable.EmptyState
import com.project.posapp.utils.toRupiah

@Composable
fun CartPanel(
    items: List<CartItem>,
    cartCount: Int,
    total: Long,
    customerType: CustomerType,
    onIncrease: (Product) -> Unit,
    onDecrease: (Product) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Large),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Text(
                text = "Keranjang ($cartCount)",
                style =
                    MaterialTheme.typography.titleLarge
            )
        }

        HorizontalDivider(
            color =
                MaterialTheme.colorScheme.outlineVariant
        )

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    icon = Icons.Outlined.ShoppingCart,
                    title = "Keranjang masih kosong",
                    description = "Pilih produk untuk menambahkannya ke keranjang."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(Spacing.Standard),
                verticalArrangement =
                    Arrangement.spacedBy(Spacing.Standard)
            ) {
                items(
                    items = items,
                    key = { it.product.id }
                ) { item ->

                    CartItemCard(
                        item = item,
                        customerType = customerType,
                        onIncrease = {
                            onIncrease(item.product)
                        },
                        onDecrease = {
                            onDecrease(item.product)
                        }
                    )
                }
            }
        }



        HorizontalDivider(
            color =
                MaterialTheme.colorScheme.outlineVariant
        )

        Column(
            modifier = Modifier.padding(Spacing.Standard)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Text(
                    text = total.toRupiah(),
                    style =
                        MaterialTheme.typography.displaySmall,
                    color =
                        MaterialTheme.colorScheme.primary
                )
            }

            Button(
                enabled = items.isNotEmpty(),
                onClick = {
                    // Belum ada proses pembayaran.
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Standard)
            ) {
                Text(
                    text = "Bayar ${total.toRupiah()}"
                )
            }

            OutlinedButton(
                onClick = onClear,
                enabled = items.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Tight)
            ) {
                Text("Kosongkan Keranjang")
            }
        }
    }
}