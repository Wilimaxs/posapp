package com.project.posapp.core.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.posapp.model.ApiResponse
import retrofit2.Response

object ErrorMapper {

    private val gson = Gson()

    fun <T> map(response: Response<ApiResponse<T>>): NetworkResult.Error {
        val code = response.code()

        val errorBody = runCatching {
            response.errorBody()?.string()
        }.getOrNull()

        val apiError = runCatching {
            val type = object : TypeToken<ApiResponse<Any?>>() {}.type
            gson.fromJson<ApiResponse<Any?>>(errorBody, type)
        }.getOrNull()

        return NetworkResult.Error(
            code = code,
            message = apiError?.message
                ?.takeIf { it.isNotBlank() }
                ?: fallbackMessage(code),
            errors = apiError?.errors
        )
    }

    private fun fallbackMessage(code: Int): String {
        return when (code) {
            400 -> "Permintaan tidak valid."
            401 -> "Sesi telah berakhir."
            403 -> "Akses ditolak."
            404 -> "Data tidak ditemukan."
            422 -> "Data tidak valid."
            in 500..599 -> "Terjadi kesalahan pada server."
            else -> "Terjadi kesalahan."
        }
    }
}