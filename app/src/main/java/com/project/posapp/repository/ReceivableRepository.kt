package com.project.posapp.repository

import com.project.posapp.core.network.ApiService
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.core.network.apiSafeCall
import com.project.posapp.model.Receivable
import com.project.posapp.model.ReceivableDetail
import com.project.posapp.model.ReceivableSummary
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceivableRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getSummary(): NetworkResult<ReceivableSummary> {
        return apiSafeCall {
            apiService.getReceivableSummary()
        }
    }

    suspend fun getReceivables(
        page: Int,
        search: String?,
        dueStatus: String?,
        sort: String?
    ): NetworkResult<List<Receivable>> {
        return apiSafeCall {
            apiService.getReceivables(
                page = page,
                search = search,
                dueStatus = dueStatus,
                sort = sort
            )
        }
    }

    suspend fun getDetail(
        saleId: Long
    ): NetworkResult<ReceivableDetail> {
        return apiSafeCall {
            apiService.getReceivableDetail(
                saleId = saleId
            )
        }
    }
}