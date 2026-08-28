package com.project.posapp.core.network

import com.project.posapp.model.ApiResponse
import com.project.posapp.model.HistoryDetail
import com.project.posapp.model.HistorySummary
import com.project.posapp.model.HistoryTransaction
import com.project.posapp.model.PosCheckoutPreview
import com.project.posapp.model.PosCustomer
import com.project.posapp.model.PosPayment
import com.project.posapp.model.PosProduct
import com.project.posapp.model.ProductCategory
import com.project.posapp.model.Receivable
import com.project.posapp.model.ReceivableDetail
import com.project.posapp.model.ReceivablePayment
import com.project.posapp.model.ReceivableSummary
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    // POS API
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

    @POST(value = "v1/payments")
    suspend fun createPayment(
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Response<ApiResponse<PosPayment>>

    @POST("v1/checkout/preview/{saleId}/cancel")
    suspend fun cancelCheckoutPreview(
        @Path("saleId") saleId: Long
    ): Response<ApiResponse<Unit>>

    // HISTORY API
    @GET("v1/history/summary")
    suspend fun getHistorySummary(): Response<ApiResponse<HistorySummary>>

    @GET("v1/history")
    suspend fun getHistory(
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null,
        @Query("date_filter") dateFilter: String? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("payment_status") paymentStatus: String? = null
    ): Response<ApiResponse<List<HistoryTransaction>>>

    @GET("v1/history/{invoiceNumber}")
    suspend fun getHistoryDetail(
        @Path("invoiceNumber") invoiceNumber: String
    ): Response<ApiResponse<HistoryDetail>>

    // PIUTANG API
    @GET("v1/receivables/summary")
    suspend fun getReceivableSummary(
    ): Response<ApiResponse<ReceivableSummary>>

    @GET("v1/receivables")
    suspend fun getReceivables(
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null,
        @Query("due_status") dueStatus: String? = null,
        @Query("sort") sort: String? = null
    ): Response<ApiResponse<List<Receivable>>>

    @GET("v1/receivables/{saleId}")
    suspend fun getReceivableDetail(
        @Path("saleId") saleId: Long
    ): Response<ApiResponse<ReceivableDetail>>

    @POST("v1/receivables/{saleId}/payments")
    suspend fun createReceivablePayment(
        @Path("saleId") saleId: Long,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Response<ApiResponse<ReceivablePayment>>
}