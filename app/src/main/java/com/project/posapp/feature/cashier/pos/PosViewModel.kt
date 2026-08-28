package com.project.posapp.feature.cashier.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.onError
import com.project.posapp.core.network.onSuccess
import com.project.posapp.model.PosCustomer
import com.project.posapp.model.PosProduct
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

    private val _uiState = MutableStateFlow(value = PosUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var requestJob: Job? = null

    fun loadPos() {
        loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            categories = result.data
                        )
                    }
                }.onError {
                    // Untuk sekarang tidak perlu menggagalkan seluruh POS
                }
        }
    }

    fun loadProducts() {
        fetchProducts(page = 1)
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasNextPage) {
            return
        }
        fetchProducts(
            page = state.currentPage + 1,
            append = true
        )
    }

    private fun fetchProducts(
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

            repository.getProducts(
                page = page,
                search = state.searchQuery.trim().takeIf { it.length >= 4 },
                categoryId = state.selectedCategoryId
            ).onSuccess { result ->
                _uiState.update { current ->
                    val products = result.data.filter(predicate = PosProduct::isActive)

                    current.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        products = if (append) {
                            (current.products + products).distinctBy(selector = PosProduct::id)
                        } else {
                            products
                        },
                        currentPage = result.meta?.currentPage ?: page,
                        lastPage = result.meta?.lastPage ?: current.lastPage,
                        errorMessage = null
                    )
                }
            }.onError { result ->
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

    fun onSearchChange(query: String) {
        searchJob?.cancel()
        requestJob?.cancel()

        _uiState.update {
            it.copy(
                searchQuery = query,
                isLoading = false,
                isLoadingMore = false
            )
        }

        val trimmedQuery = query.trim()

        if (trimmedQuery.isNotEmpty() && trimmedQuery.length < 4) {
            return
        }

        searchJob = viewModelScope.launch {
            delay(timeMillis = 400)
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


    fun addProduct(product: PosProduct) {
        if (product.stock <= 0) return

        _uiState.update { state ->

            val quantity = state.cart[product.id]?.quantity ?: 0

            if (quantity >= product.stock) {
                return@update state
            }

            state.copy(
                cart = state.cart + (
                        product.id to CartItem(
                            product = product,
                            quantity = quantity + 1
                        ))
            )
        }
    }

    fun decreaseQuantity(product: PosProduct) {
        _uiState.update { state ->
            val item = state.cart[product.id] ?: return@update state

            state.copy(
                cart = if (item.quantity <= 1) {
                    state.cart - product.id
                } else {
                    state.cart + (
                            product.id to item.copy(quantity = item.quantity - 1)
                            )
                }
            )
        }
    }

    fun onCustomerTypeChange(customerType: CustomerType) {
        if (_uiState.value.customerType == customerType) {
            return
        }

        _uiState.update {
            it.copy(
                customerType = customerType,
                selectedMember = null,
                cart = emptyMap()
            )
        }
        loadProducts()
    }

    fun showCustomerPicker() {
        _uiState.update {
            it.copy(showCustomerPicker = true)
        }
    }

    fun hideCustomerPicker() {
        _uiState.update {
            it.copy(showCustomerPicker = false)
        }
    }

    fun showPaymentPreview() {
        if (_uiState.value.cart.isEmpty()) {
            return
        }

        _uiState.update {
            it.copy(
                showPaymentPreview = true
            )
        }
    }

    fun hidePaymentPreview() {
        _uiState.update {
            it.copy(
                showPaymentPreview = false
            )
        }
    }

    fun selectMember(customer: PosCustomer) {
        _uiState.update {
            it.copy(
                selectedMember = customer,
                showCustomerPicker = false
            )
        }
    }

    fun increaseQuantity(product: PosProduct) {
        addProduct(product)
    }

    fun clearCart() {
        _uiState.update {
            it.copy(cart = emptyMap())
        }
    }

    fun resetTransaction() {
        _uiState.update {
            it.copy(
                cart = emptyMap(),
                showPaymentPreview = false
            )
        }
        loadProducts()
    }
}