package com.project.posapp.feature.cashier.pos.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.model.PosCustomer
import com.project.posapp.repository.PosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosCustomerViewModel @Inject constructor(
    private val repository: PosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = PosCustomerUiState())

    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var requestJob: Job? = null

    fun loadCustomers(selectedMember: PosCustomer? = null) {
        if (selectedMember != null) {
            _uiState.update {
                it.copy(
                    selectedCustomer = it.selectedCustomer ?: selectedMember
                )
            }
        }
        fetchCustomers(page = 1)
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasNextPage) {
            return
        }
        fetchCustomers(
            page = state.currentPage + 1,
            append = true
        )
    }

    private fun fetchCustomers(
        page: Int,
        append: Boolean = false
    ) {
        requestJob?.cancel()

        val state = _uiState.value

        requestJob = viewModelScope.launch {
            _uiState.update {
                if (append) {
                    it.copy(isLoadingMore = true)
                } else {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }

            val result = repository.getCustomers(
                page = page,
                search = state.searchQuery.trim().takeIf(String::isNotEmpty)
            )

            when (result) {
                is NetworkResult.Success -> {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            customers = if (append) {
                                (current.customers + result.data)
                                    .distinctBy(PosCustomer::id)
                            } else {
                                result.data
                            },
                            currentPage = result.meta?.currentPage ?: page,
                            lastPage = result.meta?.lastPage ?: current.lastPage,
                            errorMessage = null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = if (append) {
                                it.errorMessage
                            } else {
                                result.message
                            }
                        )
                    }
                }
            }
        }
    }

    fun onSearchChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        requestJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(timeMillis = 400)
            loadCustomers()
        }
    }

    fun selectCustomer(customer: PosCustomer) {
        _uiState.update { it.copy(selectedCustomer = customer) }
    }

    fun reset() {
        searchJob?.cancel()
        requestJob?.cancel()
        _uiState.value = PosCustomerUiState()
    }
}