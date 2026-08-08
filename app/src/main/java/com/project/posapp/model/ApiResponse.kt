package com.project.posapp.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val meta: ApiMeta? = null,
    val errors: Map<String, List<String>>? = null
)

data class ApiMeta(
    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("last_page")
    val lastPage: Int,

    @SerializedName("per_page")
    val perPage: Int,

    val total: Int
)