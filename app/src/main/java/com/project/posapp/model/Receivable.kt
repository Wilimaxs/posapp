package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class Receivable(
    @SerializedName(value = "sale_id")
    val saleId: Long? = null,
    @SerializedName(value = "invoice_number")
    val invoiceNumber: String? = null,
    @SerializedName(value = "customer")
    val customer: ReceivableCustomer? = null,
    @SerializedName(value = "remaining_balance")
    val remainingBalance: Long? = null,
    @SerializedName(value = "due_date")
    val dueDate: String? = null,
    @SerializedName(value = "due_status")
    val dueStatus: String? = null,
    @SerializedName(value = "created_at")
    val createdAt: String? = null
)

data class ReceivableCustomer(
    @SerializedName(value = "id")
    val id: Long? = null,
    @SerializedName(value = "customer_code")
    val customerCode: String? = null,
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "phone")
    val phone: String? = null,
    @SerializedName(value = "address")
    val address: String? = null
)