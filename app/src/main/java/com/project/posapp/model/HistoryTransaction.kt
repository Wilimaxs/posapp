package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class HistoryTransaction(
    @SerializedName(value = "sale_id")
    val saleId: Long? = null,
    @SerializedName(value = "invoice_number")
    val invoiceNumber: String? = null,
    @SerializedName(value = "created_at")
    val createdAt: String? = null,
    @SerializedName(value = "customer")
    val customer: HistoryTransactionCustomer? = null,
    @SerializedName(value = "customer_type")
    val customerType: String? = null,
    @SerializedName(value = "total_after_discount")
    val totalAfterDiscount: Long? = null,
    @SerializedName(value = "payment_status")
    val paymentStatus: String? = null,
    @SerializedName(value = "payment_method")
    val paymentMethod: String? = null,
    @SerializedName(value = "remaining_balance")
    val remainingBalance: Long? = null
)

data class HistoryTransactionCustomer(
    @SerializedName(value = "name")
    val name: String? = null
)