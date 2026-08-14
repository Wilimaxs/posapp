package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class PosProduct(
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
)

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