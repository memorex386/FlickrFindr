package com.example.flickerexample.network

import com.example.flickerexample.models.PhotoSearchResults

object PhotoRepository : BaseRepository() {

    suspend fun getPhotos(searchString : String) : PhotoSearchResults?{

        val photosResponse = safeApiCall(
            call = {NetworkFactory.flickrApi.getPhotosFromSearch(searchString).await()},
            errorMessage = "Error Fetching Photos"
        )

        return photosResponse?.apply {
            searchTerm = searchString
            photos?.apply {
                searchTerm = searchString
            }
        }

    }

}
