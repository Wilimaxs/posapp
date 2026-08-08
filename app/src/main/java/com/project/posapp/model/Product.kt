package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Long,
    val sku: String,
    val barcode: String?,
    val name: String,

    @SerializedName("image_url")
    val imageUrl: String?,

    val category: ProductCategory,
    val price: ProductPrice,
    val stock: Int,

    @SerializedName("minimum_stock")
    val minimumStock: Int,

    val discount: ProductDiscount?,

    @SerializedName("is_active")
    val isActive: Boolean
) {
    val hasGuestDiscount: Boolean
        get() = discount?.customerScope.equals("guest", ignoreCase = true) &&
                (discount?.value ?: 0L) > 0L

    val guestDiscountPrice: Long
        get() = if (hasGuestDiscount) {
            (price.normal - (discount?.value ?: 0L))
                .coerceAtLeast(0L)
        } else {
            price.normal
        }

    fun guestLineTotal(quantity: Int): Long {
        if (quantity <= 0) return 0L

        return if (hasGuestDiscount) {
            guestDiscountPrice +
                    (price.normal * (quantity - 1))
        } else {
            price.normal * quantity
        }
    }
}

data class ProductCategory(
    val id: Long,
    val name: String
)

data class ProductPrice(
    val normal: Long,
    val grocier: Long
)

data class ProductDiscount(
    val id: Long,
    val name: String,
    val value: Long,

    @SerializedName("customer_scope")
    val customerScope: String
)