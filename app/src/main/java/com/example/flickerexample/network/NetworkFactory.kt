package com.example.flickerexample.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkFactory {

    private val okHttpClient = OkHttpClient().newBuilder().build()


    //Flickr api = https://api.flickr.com/services/rest/?method=flickr.galleries.getPhotos&api_key=APIKEY&gallery_id=66911286-72157647277042064&format=json&nojsoncallback=1
    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.flickr.com/services/rest/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val flickrApi : FlickrApi = retrofit().create(FlickrApi::class.java)
}