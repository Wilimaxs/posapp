package com.project.posapp.feature.cashier.pos

import com.project.posapp.model.Product
import com.project.posapp.model.ProductCategory

data class PosUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,

    val products: List<Product> = emptyList(),

    val cart: Map<Long, CartItem> = emptyMap(),

    val categories: List<ProductCategory> = emptyList(),

    val searchQuery: String = "",

    val selectedCategoryId: Long? = null,
    val selectedCategoryName: String = "Semua",

    val currentPage: Int = 1,
    val lastPage: Int = 1,

    val errorMessage: String? = null
) {

    val cartItems: List<CartItem>
        get() = cart.values.toList()

    val cartCount: Int
        get() = cart.values.sumOf { it.quantity }

    val total: Long
        get() = cart.values.sumOf {
            it.product.guestLineTotal(it.quantity)
        }

    val hasNextPage: Boolean
        get() = currentPage < lastPage
}

data class CartItem(
    val product: Product,
    val quantity: Int
)