package com.example.flickerexample.network

import androidx.lifecycle.MutableLiveData
import com.example.flickerexample.models.photo_info.PhotoInfoResponse
import com.example.flickerexample.models.photos.PhotoItem
import com.example.flickerexample.models.photos.PhotoSearchResults
import com.example.flickerexample.models.photos.SearchQuery
import com.example.flickerexample.room.flickerDB

object PhotoRepository : BaseRepository() {

    val lastResult = MutableLiveData<PhotoSearchResults>()

    suspend fun getPhotoInfo(photo: PhotoItem): PhotoInfoResponse? {
        val photosResponse = safeApiCall(
            call = { NetworkFactory.flickrApi.getPhotoInfo(photo.id, photo.secret).await() },
            errorMessage = "Error Fetching Photo Info"
        )

        return photosResponse
    }

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
