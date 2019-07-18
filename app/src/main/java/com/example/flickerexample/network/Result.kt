package com.example.flickerexample.network

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    inline fun ifError(call: (Exception) -> Unit): Result<T> {
        if (this is Error) call(this.exception)
        return this
    }

    val resultElseNull
        get() = if (this is Success) data else null

    inline fun ifSuccess(call: (T) -> Unit): Result<T> {
        if (this is Success) call(this.data)
        return this
    }
}