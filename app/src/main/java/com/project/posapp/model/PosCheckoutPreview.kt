package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class PosCheckoutPreview(
    @SerializedName(value = "sale_id")
    val saleId: Long? = null,
    @SerializedName(value = "status")
    val status: String? = null,
    @SerializedName(value = "expires_at")
    val expiresAt: String? = null,
    @SerializedName(value = "customer")
    val customer: PosCheckoutPreviewCustomer? = null,
    @SerializedName(value = "customer_type")
    val customerType: String? = null,
    @SerializedName(value = "items")
    val items: List<PosCheckoutPreviewItem>? = null,
    @SerializedName(value = "total_before_discount")
    val totalBeforeDiscount: Long? = null,
    @SerializedName(value = "total_discount")
    val totalDiscount: Long? = null,
    @SerializedName(value = "total_after_discount")
    val totalAfterDiscount: Long? = null,
)

data class PosCheckoutPreviewCustomer(
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "phone")
    val phone: String? = null,
)

data class PosCheckoutPreviewItem(
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "quantity")
    val quantity: Int? = null,
    @SerializedName(value = "unit_price")
    val unitPrice: Long? = null,
    @SerializedName(value = "discount")
    val discount: PosCheckoutPreviewDiscount? = null,
    @SerializedName(value = "subtotal")
    val subtotal: Long? = null,
    @SerializedName(value = "subtotal_after_discount")
    val subtotalAfterDiscount: Long? = null,
)

data class PosCheckoutPreviewDiscount(
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "value")
    val value: Long? = null,
)
