package com.project.posapp.feature.cashier.history

import com.project.posapp.model.HistoryDetail
import com.project.posapp.model.HistorySummary
import com.project.posapp.model.HistoryTransaction
import com.project.posapp.utils.extensions.toLocalDateOrNull

data class HistoryUiState(
    val summary: HistorySummary? = null,
    val transactions: List<HistoryTransaction> = emptyList(),
    val detail: HistoryDetail? = null,

    val selectedInvoiceNumber: String? = null,

    val searchQuery: String = "",
    val dateFilter: HistoryDateFilter = HistoryDateFilter.TODAY,
    val startDate: String = "",
    val endDate: String = "",
    val paymentStatus: HistoryPaymentStatus? = null,

    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val totalTransactions: Int = 0,

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

    val canApplyCustomFilter: Boolean
        get() {
            val start = startDate.toLocalDateOrNull()
            val end = endDate.toLocalDateOrNull()

            return start != null &&
                    end != null &&
                    !end.isBefore(start)
        }
}

enum class HistoryDateFilter(
    val apiValue: String,
    val label: String
) {
    ALL("all", "Semua waktu"),
    TODAY("today", "Hari ini"),
    YESTERDAY("yesterday", "Kemarin"),
    LAST_7_DAYS("last_7_days", "7 hari terakhir"),
    THIS_MONTH("this_month", "Bulan ini"),
    CUSTOM("custom", "Rentang tanggal")
}

enum class HistoryPaymentStatus(
    val apiValue: String,
    val label: String
) {
    PAID("paid", "Lunas"),
    PARTIAL("partial", "Sebagian")
}