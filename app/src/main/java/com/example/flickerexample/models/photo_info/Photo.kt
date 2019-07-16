package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Photo(

	@Json(name="server")
	val server: String? = null,

	@Json(name="dateuploaded")
	val dateuploaded: String? = null,


	@Json(name="safety_level")
	val safetyLevel: String? = null,

	@Json(name="usage")
	val usage: Usage? = null,

	@Json(name="description")
	val description: Description? = null,

	@Json(name="secret")
	val secret: String? = null,

	@Json(name="media")
	val media: String? = null,

	@Json(name="title")
	val title: Title? = null,

	@Json(name="urls")
	val urls: Urls? = null,

	@Json(name="editability")
	val editability: Editability? = null,

	@Json(name="originalsecret")
	val originalsecret: String? = null,

	@Json(name="farm")
	val farm: Int? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="views")
	val views: String? = null,

	@Json(name="owner")
	val owner: Owner? = null,

	@Json(name="comments")
	val comments: Comments? = null,

	@Json(name="visibility")
	val visibility: Visibility? = null,

	@Json(name="publiceditability")
	val publiceditability: Publiceditability? = null,

	@Json(name="rotation")
	val rotation: Int? = null,

	@Json(name="dates")
	val dates: Dates? = null,

	@Json(name="people")
	val people: People? = null,

	@Json(name="tags")
	val tags: Tags? = null,

	@Json(name="license")
	val license: String? = null,

	@Json(name="originalformat")
	val originalformat: String? = null,

	@Json(name="isfavorite")
	val isfavorite: Int? = null
): Parcelable