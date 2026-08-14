package com.project.posapp.repository

import com.project.posapp.core.network.ApiService
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.core.network.apiSafeCall
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
}