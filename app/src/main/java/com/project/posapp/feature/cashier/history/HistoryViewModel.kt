package com.project.posapp.feature.cashier.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.onError
import com.project.posapp.core.network.onSuccess
import com.project.posapp.model.HistoryTransaction
import com.project.posapp.repository.HistoryRepository
import com.project.posapp.utils.extensions.toLocalDateOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())

    val uiState = _uiState.asStateFlow()

    private var summaryJob: Job? = null
    private var listJob: Job? = null
    private var detailJob: Job? = null
    private var searchJob: Job? = null

    init {
        loadHistory()
    }

    fun loadHistory() {
        loadSummary()
        loadTransactions()
    }

    fun loadSummary() {
        summaryJob?.cancel()

        summaryJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSummaryLoading = true,
                    summaryErrorMessage = null
                )
            }

            repository.getSummary()
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            summary = result.data,
                            isSummaryLoading = false,
                            summaryErrorMessage = null
                        )
                    }
                }
                .onError { result ->
                    _uiState.update {
                        it.copy(
                            isSummaryLoading = false,
                            summaryErrorMessage = result.message
                        )
                    }
                }
        }
    }

    fun loadTransactions() {
        fetchTransactions(page = 1)
    }

    fun loadNextPage() {
        val state = _uiState.value

        if (
            state.isListLoading ||
            state.isLoadingMore ||
            !state.hasNextPage
        ) {
            return
        }

        fetchTransactions(
            page = state.currentPage + 1,
            append = true
        )
    }

    private fun fetchTransactions(
        page: Int,
        append: Boolean = false
    ) {
        listJob?.cancel()

        if (!append) {
            detailJob?.cancel()
        }

        val state = _uiState.value

        listJob = viewModelScope.launch {
            _uiState.update {
                if (append) {
                    it.copy(isLoadingMore = true)
                } else {
                    it.copy(
                        isListLoading = true,
                        listErrorMessage = null
                    )
                }
            }

            repository.getHistory(
                page = page,
                search = state.searchQuery
                    .trim()
                    .takeIf(String::isNotEmpty),
                dateFilter = state.dateFilter.apiValue,
                startDate = state.startDate.takeIf {
                    state.dateFilter == HistoryDateFilter.CUSTOM
                },
                endDate = state.endDate.takeIf {
                    state.dateFilter == HistoryDateFilter.CUSTOM
                },
                paymentStatus = state.paymentStatus?.apiValue
            ).onSuccess { result ->
                val transactions = if (append) {
                    (_uiState.value.transactions + result.data)
                        .distinctBy(HistoryTransaction::invoiceNumber)
                } else {
                    result.data
                }

                val selectedInvoiceNumber = if (append) {
                    _uiState.value.selectedInvoiceNumber
                } else {
                    transactions.firstOrNull()?.invoiceNumber
                }

                _uiState.update {
                    it.copy(
                        transactions = transactions,
                        selectedInvoiceNumber = selectedInvoiceNumber,
                        detail = if (append) it.detail else null,
                        currentPage = result.meta?.currentPage ?: page,
                        lastPage = result.meta?.lastPage ?: page,
                        totalTransactions = result.meta?.total
                            ?: transactions.size,
                        isListLoading = false,
                        isLoadingMore = false,
                        listErrorMessage = null,
                        detailErrorMessage = null,
                        isDetailLoading = if (append) {
                            it.isDetailLoading
                        } else {
                            false
                        },

                        )
                }

                if (!append && selectedInvoiceNumber != null) {
                    loadDetail(selectedInvoiceNumber)
                }
            }.onError { result ->
                _uiState.update {
                    it.copy(
                        transactions = if (append) {
                            it.transactions
                        } else {
                            emptyList()
                        },
                        selectedInvoiceNumber = if (append) {
                            it.selectedInvoiceNumber
                        } else {
                            null
                        },
                        detail = if (append) it.detail else null,
                        isListLoading = false,
                        isLoadingMore = false,
                        listErrorMessage = if (append) {
                            it.listErrorMessage
                        } else {
                            result.message
                        },
                        isDetailLoading = if (append) {
                            it.isDetailLoading
                        } else {
                            false
                        },
                    )
                }
            }
        }
    }

    fun selectTransaction(invoiceNumber: String) {
        val state = _uiState.value

        if (
            invoiceNumber == state.selectedInvoiceNumber &&
            state.detail != null
        ) {
            return
        }

        _uiState.update {
            it.copy(
                selectedInvoiceNumber = invoiceNumber,
                detail = null,
                detailErrorMessage = null
            )
        }

        loadDetail(invoiceNumber)
    }

    fun loadDetail(invoiceNumber: String? = _uiState.value.selectedInvoiceNumber) {
        if (invoiceNumber == null) {
            return
        }

        detailJob?.cancel()

        detailJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDetailLoading = true,
                    detailErrorMessage = null
                )
            }

            repository.getDetail(invoiceNumber)
                .onSuccess { result ->
                    if (_uiState.value.selectedInvoiceNumber == invoiceNumber) {
                        _uiState.update {
                            it.copy(
                                detail = result.data,
                                isDetailLoading = false,
                                detailErrorMessage = null
                            )
                        }
                    }
                }
                .onError { result ->
                    if (_uiState.value.selectedInvoiceNumber == invoiceNumber) {
                        _uiState.update {
                            it.copy(
                                detail = null,
                                isDetailLoading = false,
                                detailErrorMessage = result.message
                            )
                        }
                    }
                }
        }
    }

    fun onSearchChange(query: String) {
        searchJob?.cancel()
        listJob?.cancel()

        val limitedQuery = query.take(255)

        _uiState.update {
            it.copy(
                searchQuery = limitedQuery,
                isListLoading = false,
                isLoadingMore = false
            )
        }

        val trimmedQuery = limitedQuery.trim()

        if (
            trimmedQuery.isNotEmpty() &&
            trimmedQuery.length < 4
        ) {
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)
            loadTransactions()
        }
    }

    fun onDateFilterChange(filter: HistoryDateFilter) {
        searchJob?.cancel()

        _uiState.update {
            it.copy(
                dateFilter = filter,
                startDate = if (filter == HistoryDateFilter.CUSTOM) {
                    it.startDate
                } else {
                    ""
                },
                endDate = if (filter == HistoryDateFilter.CUSTOM) {
                    it.endDate
                } else {
                    ""
                }
            )
        }

        if (filter != HistoryDateFilter.CUSTOM) {
            loadTransactions()
        }
    }

    fun onStartDateChange(date: String) {
        _uiState.update {
            val currentEndDate = it.endDate.toLocalDateOrNull()
            val newStartDate = date.toLocalDateOrNull()

            it.copy(
                startDate = date,
                endDate = if (
                    currentEndDate != null &&
                    newStartDate != null &&
                    currentEndDate.isBefore(newStartDate)
                ) {
                    ""
                } else {
                    it.endDate
                }
            )
        }
    }

    fun onEndDateChange(date: String) {
        _uiState.update {
            it.copy(endDate = date)
        }
    }

    fun applyCustomDateFilter() {
        val state = _uiState.value

        if (
            state.dateFilter != HistoryDateFilter.CUSTOM ||
            !state.canApplyCustomFilter
        ) {
            return
        }

        loadTransactions()
    }

    fun onPaymentStatusChange(
        status: HistoryPaymentStatus?
    ) {
        searchJob?.cancel()

        _uiState.update {
            it.copy(paymentStatus = status)
        }

        loadTransactions()
    }
}