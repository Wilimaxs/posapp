package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
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
import com.project.posapp.feature.cashier.pos.pricing.basePriceFor
import com.project.posapp.feature.cashier.pos.pricing.discountPriceFor
import com.project.posapp.feature.cashier.pos.pricing.hasDiscountFor
import com.project.posapp.feature.cashier.pos.pricing.lineTotal
import com.project.posapp.ui.theme.Radius
import com.project.posapp.utils.toRupiah

@Composable
fun CartItemCard(
    item: CartItem,
    customerType: CustomerType,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val product = item.product

    val basePrice =
        product.basePriceFor(customerType)

    val hasDiscount =
        product.hasDiscountFor(customerType)

    val priceLabel = when (customerType) {
        CustomerType.GUEST -> "Harga normal"
        CustomerType.MEMBER -> "Harga grosir"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme
                    .surfaceContainerLowest,
                RoundedCornerShape(Radius.Medium)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(Radius.Medium)
            )
            .padding(Spacing.Compact)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = product.name,
                style =
                    MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = product.lineTotal(
                    quantity = item.quantity,
                    customerType = customerType
                ).toRupiah(),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            text = priceLabel,
            modifier = Modifier.padding(
                top = Spacing.Tight
            ),
            style = MaterialTheme.typography.labelSmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.Compact),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            if (hasDiscount) {
                Column {
                    Text(
                        text = "1 item diskon: ${
                            product.discountPriceFor(
                                customerType = customerType
                            ).toRupiah()
                        }",
                        style =
                            MaterialTheme.typography.labelMedium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    if (item.quantity > 1) {
                        Text(
                            text = "${item.quantity - 1} item lainnya: ${
                                basePrice.toRupiah()
                            } / item",
                            style =
                                MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )
                    }
                }

            } else {
                Text(
                    text = "${basePrice.toRupiah()} / item",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme
                        .onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onDecrease
                ) {
                    Icon(
                        imageVector =
                            if (item.quantity == 1) {
                                Icons.Outlined.Delete
                            } else {
                                Icons.Outlined.Remove
                            },
                        contentDescription = "Kurangi"
                    )
                }

                Text(
                    text = item.quantity.toString(),
                    style =
                        MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = onIncrease,
                    enabled =
                        item.quantity < product.stock
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