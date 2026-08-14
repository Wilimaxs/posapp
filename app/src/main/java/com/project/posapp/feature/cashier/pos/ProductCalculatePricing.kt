package com.project.posapp.feature.cashier.pos

import com.project.posapp.model.Product

data class ProductPricing(
    val basePrice: Long,
    val discountedPrice: Long?,
    val total: Long
) {
    val hasDiscount: Boolean get() = discountedPrice != null
}

fun Product.calculatePricing(
    customerType: CustomerType,
    quantity: Int = 1
): ProductPricing {
    val basePrice = when (customerType) {
        CustomerType.GUEST -> price.normal
        CustomerType.MEMBER -> price.grocier
    }
    val activeDiscount = discount?.takeIf {
        it.value > 0L &&
                (it.customerScope.equals(
                    other = "all",
                    ignoreCase = true
                ) || it.customerScope.equals(
                    other = customerType.scope,
                    ignoreCase = true
                ))
    }

    val discountedPrice = activeDiscount?.let {
        (basePrice - it.value).coerceAtLeast(minimumValue = 0L)
    }
    val safeQuantity = quantity.coerceAtLeast(minimumValue = 0)

    val total = when {
        safeQuantity == 0 -> 0L
        discountedPrice != null -> discountedPrice + (basePrice * (safeQuantity - 1))
        else -> basePrice * safeQuantity
    }

    return ProductPricing(
        basePrice = basePrice,
        discountedPrice = discountedPrice,
        total = total
    )
}