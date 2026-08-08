package com.project.posapp.feature.cashier.pos

import com.project.posapp.model.Product

data class PosUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val products: List<Product> = emptyList(),
    val cart: Map<Long, Int> = emptyMap(),
    val searchQuery: String = "",
    val selectedCategory: String = "Semua",
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val errorMessage: String? = null
) {

    val categories: List<String>
        get() = listOf("Semua") +
                products
                    .map { it.category.name }
                    .distinct()

    val visibleProducts: List<Product>
        get() {
            val query = searchQuery.trim()

            return products.filter { product ->
                val categoryMatches =
                    selectedCategory == "Semua" ||
                            product.category.name == selectedCategory

                val searchMatches =
                    query.isBlank() ||
                            product.name.contains(query, ignoreCase = true) ||
                            product.sku.contains(query, ignoreCase = true) ||
                            product.barcode?.contains(query, ignoreCase = true) == true

                categoryMatches && searchMatches
            }
        }

    val cartItems: List<CartItem>
        get() = products.mapNotNull { product ->
            val quantity = cart[product.id] ?: 0

            if (quantity > 0) {
                CartItem(
                    product = product,
                    quantity = quantity
                )
            } else {
                null
            }
        }

    val cartCount: Int
        get() = cart.values.sum()

    val total: Long
        get() = cartItems.sumOf {
            it.product.guestLineTotal(it.quantity)
        }

    val hasNextPage: Boolean
        get() = currentPage < lastPage
}

data class CartItem(
    val product: Product,
    val quantity: Int
)