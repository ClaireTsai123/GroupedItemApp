package com.tsai.fetchrewardscodingexcercise.data.model

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Result<T>(data = data)
    class Error<T>(errorMessage: String) : Result<T>(message = errorMessage)
}
