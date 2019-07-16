package com.example.flickerexample.models.photos

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoSearchResults(

    @Json(name = "stat")
	val stat: String? = null,

    @Json(name = "code")
	val code: String? = null,

    @Json(name = "message")
	val message: String? = null,

    @Json(name = "photos")
	val photos: Photos? = null,

    var searchTerm: String = ""

) : Parcelable

val PhotoSearchResults.isSuccessful
get() = stat != "fail"