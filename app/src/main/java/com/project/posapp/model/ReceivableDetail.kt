package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class ReceivableDetail(
    @SerializedName(value = "sale_id")
    val saleId: Long? = null,
    @SerializedName(value = "invoice_number")
    val invoiceNumber: String? = null,
    @SerializedName(value = "customer")
    val customer: ReceivableCustomer? = null,
    @SerializedName(value = "items")
    val items: List<ReceivableItem> = emptyList(),
    @SerializedName(value = "total_before_discount")
    val totalBeforeDiscount: Long? = null,
    @SerializedName(value = "total_discount")
    val totalDiscount: Long? = null,
    @SerializedName(value = "total_after_discount")
    val totalAfterDiscount: Long? = null,
    @SerializedName(value = "due_status")
    val dueStatus: String? = null,
    @SerializedName(value = "cashier")
    val cashier: ReceivableUser? = null,
    @SerializedName(value = "payment_method")
    val paymentMethod: String? = null,
    @SerializedName(value = "initial_payment")
    val initialPayment: Long? = null,
    @SerializedName(value = "installment_total")
    val installmentTotal: Long? = null,
    @SerializedName(value = "total_paid")
    val totalPaid: Long? = null,
    @SerializedName(value = "remaining_balance")
    val remainingBalance: Long? = null,
    @SerializedName(value = "due_date")
    val dueDate: String? = null,
    @SerializedName(value = "receivable_payments")
    val receivablePayments: List<ReceivablePayment> = emptyList(),
    @SerializedName(value = "created_at")
    val createdAt: String? = null
)

data class ReceivableItem(
    @SerializedName(value = "product_id")
    val productId: Long? = null,
    @SerializedName(value = "name")
    val name: String? = null,
    @SerializedName(value = "quantity")
    val quantity: Int? = null,
    @SerializedName(value = "unit_price")
    val unitPrice: Long? = null,
    @SerializedName(value = "discount")
    val discount: Long? = null,
    @SerializedName(value = "subtotal")
    val subtotal: Long? = null,
    @SerializedName(value = "subtotal_after_discount")
    val subtotalAfterDiscount: Long? = null
)

data class ReceivableUser(
    @SerializedName(value = "id")
    val id: Long? = null,
    @SerializedName(value = "name")
    val name: String? = null
)

data class ReceivablePayment(
    @SerializedName(value = "id")
    val id: Long? = null,
    @SerializedName(value = "amount")
    val amount: Long? = null,
    @SerializedName(value = "user")
    val user: ReceivableUser? = null,
    @SerializedName(value = "notes")
    val notes: String? = null,
    @SerializedName(value = "created_at")
    val createdAt: String? = null
)