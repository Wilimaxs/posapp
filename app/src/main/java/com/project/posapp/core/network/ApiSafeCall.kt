package com.project.posapp.core.network

import com.project.posapp.model.ApiResponse
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> apiSafeCall(
    call: suspend () -> Response<ApiResponse<T>>
): NetworkResult<T> {
    return try {
        val response = call()

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.success && body.data != null) {
                NetworkResult.Success(
                    data = body.data,
                    message = body.message,
                    meta = body.meta
                )
            } else {
                ErrorMapper.map(response)
            }
        } else {
            ErrorMapper.map(response)
        }

    } catch (_: IOException) {
        NetworkResult.Error(
            message = "Tidak dapat terhubung ke server."
        )
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message ?: "Terjadi kesalahan."
        )
    }
}

suspend fun apiSafeCallNoData(
    call: suspend () -> Response<ApiResponse<Unit>>
): NetworkResult<Unit> {
    return try {
        val response = call()
        val body = response.body()

        if (response.isSuccessful && body?.success == true) {
            NetworkResult.Success(
                data = Unit,
                message = body.message,
                meta = body.meta
            )
        } else {
            ErrorMapper.map(response)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: IOException) {
        NetworkResult.Error(
            message = "Tidak dapat terhubung ke server."
        )
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message ?: "Terjadi kesalahan."
        )
    }
}