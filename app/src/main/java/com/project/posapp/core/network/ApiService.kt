package com.project.posapp.core.network

import com.project.posapp.model.ApiResponse
import com.project.posapp.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("v1/products")
    suspend fun getProducts(
        @Query("page") page: Int
    ): Response<ApiResponse<List<Product>>>

}