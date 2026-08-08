package com.project.posapp.feature.cashier.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.model.Product
import com.project.posapp.repository.PosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (
                val result = repository.getProducts(page = 1)
            ) {

                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            products = result.data.filter { product ->
                                product.isActive
                            },
                            currentPage = result.meta?.currentPage ?: 1,
                            lastPage = result.meta?.lastPage ?: 1
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

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoadingMore = true)
            }

            when (
                val result = repository.getProducts(
                    page = nextPage
                )
            ) {

                is NetworkResult.Success -> {
                    _uiState.update { current ->

                        val newProducts = result.data
                            .filter { product ->
                                product.isActive
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

    fun onSearchChange(value: String) {
        _uiState.update {
            it.copy(searchQuery = value)
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    fun addProduct(product: Product) {
        if (product.stock <= 0) return

        _uiState.update { state ->
            val currentQuantity =
                state.cart[product.id] ?: 0

            if (currentQuantity >= product.stock) {
                return@update state
            }

            state.copy(
                cart = state.cart +
                        (product.id to currentQuantity + 1)
            )
        }
    }

    fun increaseQuantity(product: Product) {
        addProduct(product)
    }

    fun decreaseQuantity(product: Product) {
        _uiState.update { state ->
            val currentQuantity =
                state.cart[product.id] ?: 0

            when {
                currentQuantity <= 1 -> {
                    state.copy(
                        cart = state.cart - product.id
                    )
                }

                else -> {
                    state.copy(
                        cart = state.cart +
                                (product.id to currentQuantity - 1)
                    )
                }
            }
        }
    }

    fun clearCart() {
        _uiState.update {
            it.copy(cart = emptyMap())
        }
    }
}