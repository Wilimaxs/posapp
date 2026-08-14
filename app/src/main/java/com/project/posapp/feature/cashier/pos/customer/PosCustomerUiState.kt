package com.project.posapp.feature.cashier.pos.customer

import com.project.posapp.model.PosCustomer

data class PosCustomerUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,

    val customers: List<PosCustomer> = emptyList(),
    val searchQuery: String = "",
    val selectedCustomer: PosCustomer? = null,

    val currentPage: Int = 1,
    val lastPage: Int = 1,

    val errorMessage: String? = null
) {
    val hasNextPage: Boolean get() = currentPage < lastPage
}