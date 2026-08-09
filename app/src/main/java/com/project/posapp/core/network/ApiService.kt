package com.project.posapp.core.network

import com.project.posapp.model.ApiResponse
import com.project.posapp.model.PosCustomer
import com.project.posapp.model.Product
import com.project.posapp.model.ProductCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("v1/products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null,
        @Query("category_id") categoryId: Long? = null
    ): Response<ApiResponse<List<Product>>>

    @GET("v1/categories")
    suspend fun getCategories(): Response<ApiResponse<List<ProductCategory>>>

    @GET("v1/customers")
    suspend fun getCustomers(
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null
    ): Response<ApiResponse<List<PosCustomer>>>

}