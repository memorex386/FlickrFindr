package com.example.flickerexample.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoSearchResults(

	@Json(name="stat")
	val stat: String? = null,

	@Json(name="code")
	val code: String? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="photos")
	val photos: Photos? = null

) : Parcelable

val PhotoSearchResults.isSuccessful
get() = stat != "fail"