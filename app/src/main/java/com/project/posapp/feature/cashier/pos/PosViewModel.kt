package com.project.posapp.feature.cashier.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.model.Product
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
class PosViewModel @Inject constructor(
    private val repository: PosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PosUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            when (val result = repository.getCategories()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            categories = result.data
                        )
                    }
                }

                is NetworkResult.Error -> {
                    // Untuk sekarang tidak perlu menggagalkan seluruh POS
                }
            }
        }
    }

    fun loadProducts() {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val search = state.searchQuery
                .trim()
                .takeIf { it.length >= 4 }

            when (
                val result = repository.getProducts(
                    page = 1,
                    search = search,
                    categoryId = state.selectedCategoryId
                )
            ) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,

                            products = result.data.filter { product ->
                                product.isActive
                            },

                            currentPage =
                                result.meta?.currentPage ?: 1,

                            lastPage =
                                result.meta?.lastPage ?: 1,

                            errorMessage = null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
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

        val nextPage = state.currentPage + 1

        val search = state.searchQuery
            .trim()
            .takeIf { it.length >= 4 }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoadingMore = true)
            }

            when (
                val result = repository.getProducts(
                    page = nextPage,
                    search = search,
                    categoryId = state.selectedCategoryId
                )
            ) {
                is NetworkResult.Success -> {
                    _uiState.update { current ->

                        val newProducts = result.data.filter {
                            it.isActive
                        }

                        current.copy(
                            isLoadingMore = false,

                            products = (
                                    current.products + newProducts
                                    ).distinctBy {
                                    it.id
                                },

                            currentPage =
                                result.meta?.currentPage
                                    ?: nextPage,

                            lastPage =
                                result.meta?.lastPage
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

    fun onSearchChange(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }

        searchJob?.cancel()

        val trimmedQuery = query.trim()

        if (
            trimmedQuery.isNotEmpty() &&
            trimmedQuery.length < 4
        ) {
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)

            loadProducts()
        }
    }

    fun onCategorySelected(categoryId: Long?) {
        _uiState.update {
            it.copy(
                selectedCategoryId = categoryId
            )
        }

        loadProducts()
    }


    fun addProduct(product: Product) {
        if (product.stock <= 0) return

        _uiState.update { state ->

            val currentItem = state.cart[product.id]

            val currentQuantity =
                currentItem?.quantity ?: 0

            if (currentQuantity >= product.stock) {
                return@update state
            }

            state.copy(
                cart = state.cart + (
                        product.id to CartItem(
                            product = product,
                            quantity = currentQuantity + 1
                        )
                        )
            )
        }
    }

    fun increaseQuantity(product: Product) {
        addProduct(product)
    }

    fun decreaseQuantity(product: Product) {
        _uiState.update { state ->

            val item = state.cart[product.id]
                ?: return@update state

            if (item.quantity <= 1) {
                state.copy(
                    cart = state.cart - product.id
                )
            } else {
                state.copy(
                    cart = state.cart + (
                            product.id to item.copy(
                                quantity = item.quantity - 1
                            )
                            )
                )
            }
        }
    }

    fun clearCart() {
        _uiState.update {
            it.copy(cart = emptyMap())
        }
    }
}