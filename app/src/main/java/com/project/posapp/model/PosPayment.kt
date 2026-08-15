package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class PosPayment(
    @SerializedName(value = "invoice_number")
    val invoiceNumber: String? = null,
    @SerializedName(value = "store")
    val store: PosPaymentStore? = null,
    @SerializedName(value = "user")
    val user: PosPaymentUser? = null,
    @SerializedName(value = "customer")
    val customer: PosPaymentCustomer? = null,
    @SerializedName(value = "customer_type")
    val customerType: String? = null,
    @SerializedName(value = "created_at")
    val createdAt: String? = null,
    @SerializedName(value = "items")
    val items: List<PosPaymentItem>? = null,
    @SerializedName(value = "total_before_discount")
    val totalBeforeDiscount: Long? = null,
    @SerializedName(value = "total_discount")
    val totalDiscount: Long? = null,
    @SerializedName(value = "total_after_discount")
    val totalAfterDiscount: Long? = null,
    @SerializedName(value = "initial_payment") // field yang dibayarkan user baik full ataupun partial
    val initialPayment: Long? = null,
    @SerializedName(value = "change_amount") // field kembalian user untuk ditampilkan
    val changeAmount: Long? = null,
    @SerializedName(value = "remaining_balance") // kalau DP ini keisi
    val remainingBalance: Long? = null,
    @SerializedName(value = "payment_method")
    val paymentMethod: String? = null,
    @SerializedName(value = "due_date") // kalau dp ini juga keisi
    val dueDate: String? = null,
)

data class PosPaymentStore(
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "address")
    val address: String? = null,
    @SerializedName(value = "phone")
    val phone: String? = null
)

data class PosPaymentUser(
    @SerializedName(value = "name")
    val name: String? = null,
)

data class PosPaymentCustomer(
    @SerializedName(value = "name")
    val name: String? = null,
)

data class PosPaymentItem(
    @SerializedName(value = "product_name")
    val productName: String? = null,
    @SerializedName(value = "quantity")
    val quantity: Int? = null,
    @SerializedName(value = "unit_price")
    val unitPrice: Long? = null,
    @SerializedName(value = "discount")
    val discount: Long? = null,
    @SerializedName(value = "subtotal_after_discount")
    val subtotalAfterDiscount: Long? = null,
)