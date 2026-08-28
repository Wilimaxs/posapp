package com.project.posapp.feature.cashier.receivable

import com.project.posapp.model.Receivable
import com.project.posapp.model.ReceivableDetail
import com.project.posapp.model.ReceivableSummary

data class ReceivableUiState(
    val summary: ReceivableSummary? = null,
    val receivables: List<Receivable> = emptyList(),
    val detail: ReceivableDetail? = null,

    val selectedSaleId: Long? = null,
    val showTransactionDialog: Boolean = false,
    val showPaymentDialog: Boolean = false,

    val searchQuery: String = "",
    val dueStatus: ReceivableDueStatus? = null,
    val sort: ReceivableSort = ReceivableSort.NEAREST,

    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val totalReceivables: Int = 0,

    val isSummaryLoading: Boolean = false,
    val isListLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isDetailLoading: Boolean = false,

    val summaryErrorMessage: String? = null,
    val listErrorMessage: String? = null,
    val detailErrorMessage: String? = null
) {
    val hasNextPage: Boolean
        get() = currentPage < lastPage
}

enum class ReceivableDueStatus(
    val apiValue: String,
    val label: String
) {
    TODAY(
        apiValue = "today",
        label = "Hari ini"
    ),
    OVERDUE(
        apiValue = "overdue",
        label = "Terlambat"
    ),
    ACTIVE(
        apiValue = "active",
        label = "Aktif"
    )
}

enum class ReceivableSort(
    val apiValue: String,
    val label: String
) {
    NEAREST(
        apiValue = "nearest",
        label = "Jatuh tempo terdekat"
    ),
    FARTHEST(
        apiValue = "farthest",
        label = "Jatuh tempo terjauh"
    )
}