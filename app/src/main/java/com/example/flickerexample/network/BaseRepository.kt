package com.example.flickerexample.network

import android.util.Log
import java.io.IOException

abstract class BaseRepository{

    suspend fun <T : Any> safeApiCall(call: suspend () -> retrofit2.Response<T>, errorMessage: String): T? {

        val result : Result<T> = safeApiResult(call,errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }


        return data

    }


    private suspend fun <T: Any> safeApiResult(call: suspend ()-> retrofit2.Response<T>, errorMessage: String) : Result<T>{
        val response = call.invoke()
        if(response.isSuccessful) return Result.Success(response.body()!!)

        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}