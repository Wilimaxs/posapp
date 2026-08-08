package com.project.posapp.repository

import com.project.posapp.core.network.ApiService
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.core.network.apiSafeCall
import com.project.posapp.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PosRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getProducts(
        page: Int
    ): NetworkResult<List<Product>> {
        return apiSafeCall {
            apiService.getProducts(page)
        }
    }
}