package com.project.posapp.feature.cashier.pos

import com.project.posapp.model.PosCustomer
import com.project.posapp.model.PosProduct
import com.project.posapp.model.ProductCategory

data class PosUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,

    val products: List<PosProduct> = emptyList(),
    val cart: Map<Long, CartItem> = emptyMap(),

    val categories: List<ProductCategory> = emptyList(),
    val selectedCategoryId: Long? = null,

    val customerType: CustomerType = CustomerType.GUEST,
    val selectedMember: PosCustomer? = null,

    val showCustomerPicker: Boolean = false,
    val showPaymentPreview: Boolean = false,

    val searchQuery: String = "",

    val currentPage: Int = 1,
    val lastPage: Int = 1,

    val errorMessage: String? = null
) {

    val cartItems: List<CartItem> get() = cart.values.toList()

    val cartCount: Int get() = cart.values.sumOf { it.quantity }

    val total: Long
        get() = cart.values.sumOf {
            it.product.calculatePricing(
                customerType = customerType,
                quantity = it.quantity
            ).total
        }

    val hasNextPage: Boolean get() = currentPage < lastPage
}

data class CartItem(
    val product: PosProduct,
    val quantity: Int
)

enum class CustomerType(
    val scope: String
) {
    GUEST("guest"),
    MEMBER("member")
}