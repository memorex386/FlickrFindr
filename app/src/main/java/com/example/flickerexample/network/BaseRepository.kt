package com.example.flickerexample.network

import android.util.Log

abstract class BaseRepository{

    suspend fun <T : Any> safeApiCall(call: suspend () -> retrofit2.Response<T>, errorMessage: String): T? {

        val result : Result<T> = safeApiResult(call,errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d("err", "$errorMessage & Exception - ${result.exception}")
            }
        }


        return data

    }


    suspend fun <T : Any> safeApiResult(call: suspend () -> retrofit2.Response<T>, errorMessage: String): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) return Result.Success(response.body()!!)

            return Result.Error(Exception(response.errorBody()?.string() ?: errorMessage))
        } catch (e: Exception) {
            return Result.Error(e)
        }

    }
}

