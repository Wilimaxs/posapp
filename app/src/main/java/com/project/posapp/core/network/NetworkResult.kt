package com.project.posapp.core.network

sealed class NetworkResult<out T> {

    data class Success<T>(
        val data: T,
        val message: String? = null
    ) : NetworkResult<T>()

    data class Error(
        val code: Int? = null,
        val message: String,
        val errors: Map<String, List<String>>? = null
    ) : NetworkResult<Nothing>()

}