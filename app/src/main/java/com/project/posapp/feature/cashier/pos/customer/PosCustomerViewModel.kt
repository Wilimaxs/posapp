package com.project.posapp.feature.cashier.pos.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.model.PosCustomer
import com.project.posapp.repository.PosCustomerRepository
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
    private val repository: PosCustomerRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PosCustomerUiState())

    val uiState =
        _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var requestJob: Job? = null

    fun loadCustomers(
        selectedMember: PosCustomer? = null
    ) {
        requestJob?.cancel()

        val state = _uiState.value

        requestJob = viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,

                    selectedCustomer =
                        it.selectedCustomer
                            ?: selectedMember
                )
            }

            val search = state.searchQuery
                .trim()
                .takeIf {
                    it.isNotEmpty()
                }

            when (
                val result = repository.getCustomers(
                    page = 1,
                    search = search
                )
            ) {

                is NetworkResult.Success -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,

                            customers =
                                result.data,

                            currentPage =
                                result.meta
                                    ?.currentPage
                                    ?: 1,

                            lastPage =
                                result.meta
                                    ?.lastPage
                                    ?: 1,

                            errorMessage = null
                        )
                    }
                }

                is NetworkResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage =
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
        _uiState.update {
            it.copy(
                searchQuery = query
            )
        }

        searchJob?.cancel()

        requestJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(400)

            loadCustomers()
        }
    }

    fun selectCustomer(
        customer: PosCustomer
    ) {
        _uiState.update {
            it.copy(
                selectedCustomer = customer
            )
        }
    }

    fun loadNextPage() {
        val state = _uiState.value

        if (
            state.isLoading ||
            state.isLoadingMore ||
            !state.hasNextPage
        ) {
            return
        }

        val nextPage =
            state.currentPage + 1

        val search = state.searchQuery
            .trim()
            .takeIf {
                it.isNotEmpty()
            }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoadingMore = true
                )
            }

            when (
                val result =
                    repository.getCustomers(
                        page = nextPage,
                        search = search
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.update { current ->

                        current.copy(
                            isLoadingMore = false,

                            customers = (
                                    current.customers +
                                            result.data
                                    ).distinctBy {
                                    it.id
                                },

                            currentPage =
                                result.meta
                                    ?.currentPage
                                    ?: nextPage,

                            lastPage =
                                result.meta
                                    ?.lastPage
                                    ?: current.lastPage
                        )
                    }
                }

                is NetworkResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    fun reset() {
        searchJob?.cancel()
        requestJob?.cancel()

        _uiState.value =
            PosCustomerUiState()
    }
}