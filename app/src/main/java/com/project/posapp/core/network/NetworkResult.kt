package com.project.posapp.core.network

import com.project.posapp.model.ApiMeta

sealed class NetworkResult<out T> {
    data class Success<T>(
        val data: T,
        val message: String? = null,
        val meta: ApiMeta? = null
    ) : NetworkResult<T>()
    data class Error(
        val code: Int? = null,
        val message: String,
        val errors: Map<String, List<String>>? = null
    ) : NetworkResult<Nothing>()
}

inline fun <T> NetworkResult<T>.onSuccess(
    action: (NetworkResult.Success<T>) -> Unit
): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        action(this)
    }
    return this
}

inline fun <T> NetworkResult<T>.onError(
    action: (NetworkResult.Error) -> Unit
): NetworkResult<T> {
    if (this is NetworkResult.Error) {
        action(this)
    }
    return this
}