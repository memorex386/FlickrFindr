package com.example.flickerexample.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoItem(

	@Json(name="owner")
	val owner: String? = null,

	@Json(name="server")
	val server: String,

	@Json(name="ispublic")
	val ispublic: Int? = null,

	@Json(name="isfriend")
	val isfriend: Int? = null,

	@Json(name="farm")
	val farm: Int,

	@Json(name="id")
	val id: String,

	@Json(name="secret")
	val secret: String,

	@Json(name="title")
	val title: String? = null,

	@Json(name="isfamily")
	val isfamily: Int? = null
) : Parcelable


fun PhotoItem.getPhotoUrl() =  "http://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
