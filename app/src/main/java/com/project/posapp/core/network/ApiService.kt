package com.project.posapp.core.network

import com.project.posapp.model.ApiResponse
import com.project.posapp.model.PosCheckoutPreview
import com.project.posapp.model.PosCustomer
import com.project.posapp.model.PosProduct
import com.project.posapp.model.ProductCategory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @GET(value = "v1/products")
    suspend fun getProducts(
        @Query(value = "page") page: Int = 1,
        @Query(value = "search") search: String? = null,
        @Query(value = "category_id") categoryId: Long? = null
    ): Response<ApiResponse<List<PosProduct>>>

    @GET(value = "v1/categories")
    suspend fun getCategories(): Response<ApiResponse<List<ProductCategory>>>

    @GET(value = "v1/customers")
    suspend fun getCustomers(
        @Query(value = "page") page: Int = 1,
        @Query(value = "search") search: String? = null
    ): Response<ApiResponse<List<PosCustomer>>>

    @POST(value = "v1/checkout/preview")
    suspend fun checkoutPreview(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<ApiResponse<PosCheckoutPreview>>
}