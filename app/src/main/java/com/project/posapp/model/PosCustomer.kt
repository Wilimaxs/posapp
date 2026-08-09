package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class PosCustomer(
    val id: Long,

    @SerializedName("customer_code")
    val customerCode: String,

    val name: String,
    val phone: String,
    val address: String?,

    @SerializedName("has_receivable")
    val hasReceivable: Boolean
)