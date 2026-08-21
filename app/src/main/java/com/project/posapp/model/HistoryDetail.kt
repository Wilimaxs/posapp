package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class HistoryDetail(
    @SerializedName(value = "invoice_number")
    val invoiceNumber: String? = null,
    @SerializedName(value = "status")
    val status: String? = null,
    @SerializedName(value = "created_at")
    val createdAt: String? = null,
    @SerializedName(value = "store")
    val store: HistoryStore? = null,
    @SerializedName(value = "user")
    val user: HistoryUser? = null,
    @SerializedName(value = "customer")
    val customer: HistoryCustomer? = null,
    @SerializedName(value = "customer_type")
    val customerType: String? = null,
    @SerializedName(value = "items")
    val items: List<HistoryItem> = emptyList(),
    @SerializedName(value = "total_before_discount")
    val totalBeforeDiscount: Long? = null,
    @SerializedName(value = "total_discount")
    val totalDiscount: Long? = null,
    @SerializedName(value = "total_after_discount")
    val totalAfterDiscount: Long? = null,
    @SerializedName(value = "payment")
    val payment: HistoryPayment? = null,
    @SerializedName(value = "receivable_payments")
    val receivablePayments: List<HistoryReceivablePayment> = emptyList(),
    @SerializedName(value = "notes")
    val notes: String? = null
)

data class HistoryStore(
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "address")
    val address: String? = null,
    @SerializedName(value = "phone")
    val phone: String? = null
)

data class HistoryUser(
    @SerializedName(value = "id")
    val id: Long? = null,
    @SerializedName(value = "name")
    val name: String? = null
)

data class HistoryCustomer(
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

data class HistoryItem(
    @SerializedName(value = "product_id")
    val productId: Long? = null,
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "sku")
    val sku: String? = null,
    @SerializedName(value = "barcode")
    val barcode: String? = null,
    @SerializedName(value = "quantity")
    val quantity: Int? = null,
    @SerializedName(value = "unit_price")
    val unitPrice: Long? = null,
    @SerializedName(value = "discount")
    val discount: HistoryDiscount? = null,
    @SerializedName(value = "subtotal")
    val subtotal: Long? = null,
    @SerializedName(value = "subtotal_after_discount")
    val subtotalAfterDiscount: Long? = null
)

data class HistoryDiscount(
    @SerializedName(value = "id")
    val id: Long? = null,
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "value")
    val value: Long? = null
)

data class HistoryPayment(
    @SerializedName(value = "method")
    val method: String? = null,
    @SerializedName(value = "initial_payment")
    val initialPayment: Long? = null,
    @SerializedName(value = "installment_total")
    val installmentTotal: Long? = null,
    @SerializedName(value = "total_paid")
    val totalPaid: Long? = null,
    @SerializedName(value = "change_amount")
    val changeAmount: Long? = null,
    @SerializedName(value = "remaining_balance")
    val remainingBalance: Long? = null,
    @SerializedName(value = "payment_status")
    val paymentStatus: String? = null,
    @SerializedName(value = "due_date")
    val dueDate: String? = null,
    @SerializedName(value = "paid_at")
    val paidAt: String? = null
)

data class HistoryReceivablePayment(
    @SerializedName(value = "id")
    val id: Long? = null,
    @SerializedName(value = "amount")
    val amount: Long? = null,
    @SerializedName(value = "user")
    val user: HistoryUser? = null,
    @SerializedName(value = "notes")
    val notes: String? = null,
    @SerializedName(value = "created_at")
    val createdAt: String? = null
)