package com.project.posapp.repository

import com.project.posapp.core.network.ApiService
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.core.network.apiSafeCall
import com.project.posapp.core.network.apiSafeCallNoData
import com.project.posapp.model.PosCheckoutPreview
import com.project.posapp.model.PosCustomer
import com.project.posapp.model.PosPayment
import com.project.posapp.model.PosProduct
import com.project.posapp.model.ProductCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PosRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getProducts(
        page: Int,
        search: String? = null,
        categoryId: Long? = null
    ): NetworkResult<List<PosProduct>> {
        return apiSafeCall {
            apiService.getProducts(
                page = page,
                search = search,
                categoryId = categoryId
            )
        }
    }

    suspend fun getCategories(): NetworkResult<List<ProductCategory>> {
        return apiSafeCall {
            apiService.getCategories()
        }
    }

    suspend fun getCustomers(
        page: Int = 1,
        search: String? = null
    ): NetworkResult<List<PosCustomer>> {
        return apiSafeCall {
            apiService.getCustomers(
                page = page,
                search = search
            )
        }
    }

    suspend fun checkoutPreview(
        customerId: Long?,
        items: Map<Long, Int>
    ): NetworkResult<PosCheckoutPreview> {
        val body = buildMap {
            customerId?.let { put("customer_id", it) }
            put(
                "items",
                items.map { (productId, quantity) ->
                    mapOf(
                        "product_id" to productId,
                        "quantity" to quantity
                    )
                }
            )
        }
        return apiSafeCall {
            apiService.checkoutPreview(body)
        }
    }

    suspend fun createPayment(
        saleId: Long,
        paymentAmount: Long,
        paymentMethod: String,
        dueDate: String?
    ): NetworkResult<PosPayment> {
        val body = buildMap<String, Any?> {
            put("sale_id", saleId)
            put("payment_amount", paymentAmount)
            put("payment_method", paymentMethod)
            put("due_date", dueDate)
        }

        return apiSafeCall {
            apiService.createPayment(body)
        }
    }

    suspend fun cancelCheckoutPreview(
        saleId: Long
    ): NetworkResult<Unit> {
        return apiSafeCallNoData {
            apiService.cancelCheckoutPreview(saleId)
        }
    }
}