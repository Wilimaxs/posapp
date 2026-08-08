package com.project.posapp.feature.cashier.pos.pricing

import com.project.posapp.feature.cashier.pos.CustomerType
import com.project.posapp.model.Product

fun Product.basePriceFor(
    customerType: CustomerType
): Long {
    return when (customerType) {
        CustomerType.GUEST -> price.normal
        CustomerType.MEMBER -> price.grocier
    }
}

fun Product.hasDiscountFor(
    customerType: CustomerType
): Boolean {
    val currentDiscount = discount ?: return false

    val applicableScope =
        currentDiscount.customerScope.equals(
            "all",
            ignoreCase = true
        ) ||
                currentDiscount.customerScope.equals(
                    customerType.scope,
                    ignoreCase = true
                )

    return applicableScope &&
            currentDiscount.value > 0L
}

fun Product.discountPriceFor(
    customerType: CustomerType
): Long {
    val basePrice = basePriceFor(customerType)
    val currentDiscount = discount ?: return basePrice

    if (!hasDiscountFor(customerType)) {
        return basePrice
    }

    return (
            basePrice - currentDiscount.value
            ).coerceAtLeast(0L)
}

fun Product.lineTotal(
    quantity: Int,
    customerType: CustomerType
): Long {
    if (quantity <= 0) {
        return 0L
    }

    val basePrice = basePriceFor(customerType)

    if (!hasDiscountFor(customerType)) {
        return basePrice * quantity
    }

    return discountPriceFor(customerType) +
            (basePrice * (quantity - 1))
}