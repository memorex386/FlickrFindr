package com.example.flickerexample.network

import com.example.flickerexample.models.PhotoSearchResults
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi{

    @GET("?method=flickr.photos.search$flickrParams")
    fun getPhotosFromSearch(@Query("text") searchString:String): Deferred<Response<PhotoSearchResults>>

}

private const val flickrKey = "1508443e49213ff84d566777dc211f2a"

private const val flickrParams = "&api_key=$flickrKey&format=json&nojsoncallback=1"
