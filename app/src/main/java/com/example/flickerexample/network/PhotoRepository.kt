package com.example.flickerexample.network

import androidx.lifecycle.MutableLiveData
import com.example.flickerexample.core.extensions.liveDataNotNull
import com.example.flickerexample.models.photo_info.PhotoInfoResponse
import com.example.flickerexample.models.photos.PhotoItem
import com.example.flickerexample.models.photos.PhotoSearchResults
import com.example.flickerexample.models.photos.SearchQuery
import com.example.flickerexample.room.flickerDB

object PhotoRepository : BaseRepository() {

    val lastResult = MutableLiveData<PhotoSearchResults>()

    val sortItem = PhotoSortItem.RELEVANCE.liveDataNotNull()

    var currentPage = 1

    suspend fun getPhotoInfo(photo: PhotoItem): PhotoInfoResponse? {
        val photosResponse = safeApiCall(
            call = { NetworkFactory.flickrApi.getPhotoInfo(photo.id, photo.secret).await() },
            errorMessage = "Error Fetching Photo Info"
        )

        return photosResponse
    }

    suspend fun getPhotos(
        searchString: String
    ): PhotoSearchResults? {

        if (lastResult.value?.searchTerm != searchString) {
            currentPage = 1
        }

        val photosResponse = safeApiCall(
            call = {
                NetworkFactory.flickrApi.getPhotosFromSearch(searchString, sortItem.value.sortText, currentPage).await()
            },
            errorMessage = "Error Fetching Photos"
        )

        flickerDB.searchQueryDao().insert(SearchQuery(searchString))

        photosResponse?.apply {
            photos?.page?.also { currentPage = it }
            searchTerm = searchString
            photos?.apply {
                searchTerm = searchString
            }
        }

        lastResult.postValue(photosResponse)

        return photosResponse
    }

}
