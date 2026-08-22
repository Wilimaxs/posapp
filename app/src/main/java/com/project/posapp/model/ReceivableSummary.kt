package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class ReceivableSummary(
    @SerializedName(value = "total_active")
    val totalActive: ReceivableSummaryValue = ReceivableSummaryValue(),
    @SerializedName(value = "due_today")
    val dueToday: ReceivableSummaryValue = ReceivableSummaryValue(),
    @SerializedName(value = "overdue")
    val overdue: ReceivableSummaryValue = ReceivableSummaryValue(),
    @SerializedName(value = "payments_today")
    val paymentsToday: ReceivableSummaryValue = ReceivableSummaryValue()
)

data class ReceivableSummaryValue(
    @SerializedName(value = "amount")
    val amount: Long = 0L,
    @SerializedName(value = "count")
    val count: Int = 0
)