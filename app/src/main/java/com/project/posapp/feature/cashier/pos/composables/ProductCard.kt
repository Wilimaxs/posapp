package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.project.posapp.feature.cashier.pos.pricing.pricing
import com.project.posapp.model.Product
import com.project.posapp.ui.theme.Radius
import com.project.posapp.utils.toRupiah

@Composable
fun ProductCard(
    product: Product,
    quantityInCart: Int,
    customerType: CustomerType,
    onClick: () -> Unit
) {
    val isOutOfStock = product.stock <= 0
    val isLowStock =
        product.stock in 1..product.minimumStock

    val pricing = product.pricing(customerType)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isOutOfStock) 0.55f else 1f)
            .background(
                color = if (quantityInCart > 0) {
                    MaterialTheme.colorScheme.primary
                        .copy(alpha = 0.05f)
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(Radius.Medium)
            )
            .border(
                width = 1.dp,
                color = if (quantityInCart > 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = RoundedCornerShape(Radius.Medium)
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
                    RoundedCornerShape(
                        topStart = Radius.Medium,
                        topEnd = Radius.Medium
                    )
                )
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow
                ),
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

            Text(
                text = "Stok: ${product.stock}",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Spacing.Tight)
                    .background(
                        color = MaterialTheme.colorScheme.surface
                            .copy(alpha = 0.92f),
                        shape = RoundedCornerShape(Radius.Small)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isOutOfStock) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        shape = RoundedCornerShape(Radius.Small)
                    )
                    .padding(
                        horizontal = Spacing.Tight,
                        vertical = Spacing.Micro
                    ),
                style = MaterialTheme.typography.labelSmall,
                color = if (isOutOfStock) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            if (isLowStock) {
                Text(
                    text = "Stok rendah",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(Spacing.Tight)
                        .background(
                            color = Warning,
                            shape = RoundedCornerShape(Radius.Small)
                        )
                        .padding(
                            horizontal = Spacing.Tight,
                            vertical = Spacing.Micro
                        ),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }

            if (isOutOfStock) {
                Text(
                    text = "HABIS",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .rotate(-12f)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(Radius.Default)
                        )
                        .padding(
                            horizontal = Spacing.Standard,
                            vertical = Spacing.Tight
                        ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }

        Column(
            modifier = Modifier.padding(Spacing.Compact)
        ) {

            if (quantityInCart > 0) {
                Text(
                    text = "$quantityInCart di keranjang",
                    style =
                        MaterialTheme.typography.labelMedium,
                    color =
                        MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = product.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    vertical = Spacing.Tight
                )
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
                        modifier = Modifier.padding(
                            start = Spacing.Tight
                        ),
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
                    CustomerType.GUEST ->
                        "Grosir: ${product.price.grocier.toRupiah()}"

                    CustomerType.MEMBER ->
                        "Normal: ${product.price.normal.toRupiah()}"
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}