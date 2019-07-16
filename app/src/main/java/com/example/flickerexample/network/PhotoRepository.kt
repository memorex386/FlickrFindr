package com.example.flickerexample.network

import androidx.lifecycle.MutableLiveData
import com.example.flickerexample.models.PhotoSearchResults
import com.example.flickerexample.models.SearchQuery
import com.example.flickerexample.room.flickerDB

object PhotoRepository : BaseRepository() {

    val lastResult = MutableLiveData<PhotoSearchResults>()

    suspend fun getPhotos(searchString : String) : PhotoSearchResults?{

        val photosResponse = safeApiCall(
            call = { NetworkFactory.flickrApi.getPhotosFromSearch(searchString).await() },
            errorMessage = "Error Fetching Photos"
        )

        flickerDB.searchQueryDao().insert(SearchQuery(searchString))

        photosResponse?.apply {
            searchTerm = searchString
            photos?.apply {
                searchTerm = searchString
            }
        }

        lastResult.postValue(photosResponse)

        return photosResponse
    }

}
