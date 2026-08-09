package com.project.posapp.repository

import com.project.posapp.core.network.ApiService
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.core.network.apiSafeCall
import com.project.posapp.model.PosCustomer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PosCustomerRepository @Inject constructor(
    private val apiService: ApiService
) {

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
}