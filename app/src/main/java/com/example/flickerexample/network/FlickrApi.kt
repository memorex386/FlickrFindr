package com.example.flickerexample.network

import com.example.flickerexample.models.photo_info.PhotoInfoResponse
import com.example.flickerexample.models.photos.PhotoSearchResults
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi{

    @GET("?method=flickr.photos.search$flickrParams")
    fun getPhotosFromSearch(@Query("text") searchString:String): Deferred<Response<PhotoSearchResults>>

    @GET("?method=flickr.photos.getInfo$flickrParams")
    fun getPhotoInfo(@Query("photo_id") photoId: String, @Query("secret") secret: String): Deferred<Response<PhotoInfoResponse>>

}

private const val flickrKey = "1508443e49213ff84d566777dc211f2a"

private const val flickrParams = "&api_key=$flickrKey&format=json&nojsoncallback=1"
