package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class PosCustomer(
    val id: Long,

    @SerializedName(value = "customer_code")
    val customerCode: String,

    val name: String,
    val phone: String,
    val address: String?,

    val receivable: PosCustomerReceivable?
)

data class PosCustomerReceivable(
    @SerializedName(value = "transaction_count")
    val transactionCount: Int,

    @SerializedName(value = "total_remaining_balance")
    val totalRemainingBalance: Long
)