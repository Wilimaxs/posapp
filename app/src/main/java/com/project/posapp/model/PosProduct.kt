package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class PosProduct(
    @SerializedName(value = "id")
    val id: Long,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "image_url")
    val imageUrl: String?,
    @SerializedName(value = "category")
    val category: ProductCategory,
    @SerializedName(value = "price")
    val price: ProductPrice,
    @SerializedName(value = "stock")
    val stock: Int,
    @SerializedName("minimum_stock")
    val minimumStock: Int,
    @SerializedName("discount")
    val discount: ProductDiscount?,
    @SerializedName("is_active")
    val isActive: Boolean
)

data class ProductCategory(
    @SerializedName(value = "id")
    val id: Long,
    @SerializedName(value = "name")
    val name: String
)

data class ProductPrice(
    @SerializedName(value = "normal")
    val normal: Long,
    @SerializedName(value = "grocier")
    val grocier: Long
)

data class ProductDiscount(
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "value")
    val value: Long,
    @SerializedName(value = "customer_scope")
    val customerScope: String
)