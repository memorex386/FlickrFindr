package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class People(

	@Json(name="haspeople")
	val haspeople: Int? = null
): Parcelable