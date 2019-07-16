package com.example.flickerexample.network

import com.example.flickerexample.models.PhotoSearchResults
import com.example.flickerexample.models.SearchQuery
import com.example.flickerexample.room.flickerDB

object PhotoRepository : BaseRepository() {

    suspend fun getPhotos(searchString : String) : PhotoSearchResults?{

        val photosResponse = safeApiCall(
            call = {NetworkFactory.flickrApi.getPhotosFromSearch(searchString).await()},
            errorMessage = "Error Fetching Photos"
        )

        flickerDB.searchQueryDao().insert(SearchQuery(searchString))

        return photosResponse?.apply {
            searchTerm = searchString
            photos?.apply {
                searchTerm = searchString
            }
        }

    }

}
