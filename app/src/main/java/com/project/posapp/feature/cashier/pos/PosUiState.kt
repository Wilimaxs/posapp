package com.project.posapp.feature.cashier.pos

import com.project.posapp.feature.cashier.pos.pricing.lineTotal
import com.project.posapp.model.Product
import com.project.posapp.model.ProductCategory

data class PosUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,

    val products: List<Product> = emptyList(),
    val categories: List<ProductCategory> = emptyList(),
    val cart: Map<Long, CartItem> = emptyMap(),

    val customerType: CustomerType = CustomerType.GUEST,

    val searchQuery: String = "",
    val selectedCategoryId: Long? = null,

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
            it.product.lineTotal(
                quantity = it.quantity,
                customerType = customerType
            )
        }

    val hasNextPage: Boolean
        get() = currentPage < lastPage
}

data class CartItem(
    val product: Product,
    val quantity: Int
)

enum class CustomerType(
    val scope: String
) {
    GUEST("guest"),
    MEMBER("member")
}