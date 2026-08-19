package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class PosCustomer(
    @SerializedName(value = "id")
    val id: Long? = null,
    @SerializedName(value = "customer_code")
    val customerCode: String? = null,
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "phone")
    val phone: String? = null,
    @SerializedName(value = "address")
    val address: String? = null,
    @SerializedName(value = "receivable")
    val receivable: PosCustomerReceivable? = null
)

data class PosCustomerReceivable(
    @SerializedName(value = "transaction_count")
    val transactionCount: Int,
    @SerializedName(value = "total_remaining_balance")
    val totalRemainingBalance: Long
)