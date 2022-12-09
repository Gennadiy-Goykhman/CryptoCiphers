package com.example.cryptos.data.models

sealed class ResultResponse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): ResultResponse<T>(data)
    class Error<T>(message: String,data: T? = null ): ResultResponse<T>(data,message)
}