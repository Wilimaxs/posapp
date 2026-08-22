package com.project.posapp.feature.cashier.receivable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.onError
import com.project.posapp.core.network.onSuccess
import com.project.posapp.repository.ReceivableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceivableViewModel @Inject constructor(
    private val repository: ReceivableRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ReceivableUiState()
    )

    val uiState = _uiState.asStateFlow()

    private var summaryJob: Job? = null
    private var listJob: Job? = null
    private var detailJob: Job? = null
    private var searchJob: Job? = null

    init {
        loadReceivables()
    }

    fun loadReceivables() {
        loadSummary()
        loadList()
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

    fun loadList() {
        fetchReceivables(page = 1)
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

        fetchReceivables(
            page = state.currentPage + 1,
            append = true
        )
    }

    private fun fetchReceivables(
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
                    it.copy(
                        isLoadingMore = true
                    )
                } else {
                    it.copy(
                        isListLoading = true,
                        listErrorMessage = null
                    )
                }
            }

            repository.getReceivables(
                page = page,
                search = state.searchQuery
                    .trim()
                    .takeIf(String::isNotEmpty),
                dueStatus = state.dueStatus?.apiValue,
                sort = state.sort.apiValue
            ).onSuccess { result ->
                val receivables = if (append) {
                    (
                            _uiState.value.receivables +
                                    result.data
                            ).distinctBy {
                            it.saleId
                        }
                } else {
                    result.data
                }

                val selectedSaleId = if (append) {
                    _uiState.value.selectedSaleId
                } else {
                    receivables.firstOrNull()?.saleId
                }

                _uiState.update {
                    it.copy(
                        receivables = receivables,
                        selectedSaleId = selectedSaleId,
                        detail = if (append) {
                            it.detail
                        } else {
                            null
                        },
                        showTransactionDialog = false,
                        currentPage =
                            result.meta?.currentPage ?: page,
                        lastPage =
                            result.meta?.lastPage ?: page,
                        totalReceivables =
                            result.meta?.total
                                ?: receivables.size,
                        isListLoading = false,
                        isLoadingMore = false,
                        isDetailLoading = if (append) {
                            it.isDetailLoading
                        } else {
                            false
                        },
                        listErrorMessage = null,
                        detailErrorMessage = null
                    )
                }

                if (
                    !append &&
                    selectedSaleId != null
                ) {
                    loadDetail(selectedSaleId)
                }
            }.onError { result ->
                _uiState.update {
                    it.copy(
                        receivables = if (append) {
                            it.receivables
                        } else {
                            emptyList()
                        },
                        selectedSaleId = if (append) {
                            it.selectedSaleId
                        } else {
                            null
                        },
                        detail = if (append) {
                            it.detail
                        } else {
                            null
                        },
                        showTransactionDialog = false,
                        isListLoading = false,
                        isLoadingMore = false,
                        isDetailLoading = if (append) {
                            it.isDetailLoading
                        } else {
                            false
                        },
                        listErrorMessage = if (append) {
                            it.listErrorMessage
                        } else {
                            result.message
                        }
                    )
                }
            }
        }
    }

    fun selectReceivable(
        saleId: Long
    ) {
        val state = _uiState.value

        if (
            saleId == state.selectedSaleId &&
            state.detail != null
        ) {
            return
        }

        _uiState.update {
            it.copy(
                selectedSaleId = saleId,
                detail = null,
                showTransactionDialog = false,
                detailErrorMessage = null
            )
        }

        loadDetail(saleId)
    }

    fun loadDetail(
        saleId: Long? = _uiState.value.selectedSaleId
    ) {
        if (saleId == null) {
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

            repository.getDetail(saleId)
                .onSuccess { result ->
                    if (
                        _uiState.value.selectedSaleId ==
                        saleId
                    ) {
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
                    if (
                        _uiState.value.selectedSaleId ==
                        saleId
                    ) {
                        _uiState.update {
                            it.copy(
                                detail = null,
                                isDetailLoading = false,
                                detailErrorMessage =
                                    result.message
                            )
                        }
                    }
                }
        }
    }

    fun onSearchChange(
        query: String
    ) {
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
            loadList()
        }
    }

    fun onDueStatusChange(
        status: ReceivableDueStatus?
    ) {
        searchJob?.cancel()

        _uiState.update {
            it.copy(
                dueStatus = status
            )
        }

        loadList()
    }

    fun onSortChange(
        sort: ReceivableSort
    ) {
        searchJob?.cancel()

        _uiState.update {
            it.copy(
                sort = sort
            )
        }

        loadList()
    }

    fun showTransactionDialog() {
        if (_uiState.value.detail == null) {
            return
        }

        _uiState.update {
            it.copy(
                showTransactionDialog = true
            )
        }
    }

    fun dismissTransactionDialog() {
        _uiState.update {
            it.copy(
                showTransactionDialog = false
            )
        }
    }
}