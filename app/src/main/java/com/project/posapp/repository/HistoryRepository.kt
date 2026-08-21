package com.project.posapp.repository

import com.project.posapp.core.network.ApiService
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.core.network.apiSafeCall
import com.project.posapp.model.HistoryDetail
import com.project.posapp.model.HistorySummary
import com.project.posapp.model.HistoryTransaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getSummary(): NetworkResult<HistorySummary> {
        return apiSafeCall {
            apiService.getHistorySummary()
        }
    }

    suspend fun getHistory(
        page: Int,
        search: String?,
        dateFilter: String?,
        startDate: String?,
        endDate: String?,
        paymentStatus: String?
    ): NetworkResult<List<HistoryTransaction>> {
        return apiSafeCall {
            apiService.getHistory(
                page = page,
                search = search,
                dateFilter = dateFilter,
                startDate = startDate,
                endDate = endDate,
                paymentStatus = paymentStatus
            )
        }
    }

    suspend fun getDetail(
        invoiceNumber: String
    ): NetworkResult<HistoryDetail> {
        return apiSafeCall {
            apiService.getHistoryDetail(
                invoiceNumber = invoiceNumber
            )
        }
    }
}