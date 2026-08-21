package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class HistorySummary(
    @SerializedName(value = "total_transactions")
    val totalTransactions: Int = 0,
    @SerializedName(value = "total_sales")
    val totalSales: Long = 0L,
    @SerializedName(value = "cash_payment")
    val cashPayment: Long = 0L,
    @SerializedName(value = "qris_payment")
    val qrisPayment: Long = 0L
)