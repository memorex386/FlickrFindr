package com.example.flickerexample.models.photos

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "PhotoItem")
data class PhotoItem(

	@Json(name="owner")
	@ColumnInfo(name = "owner")
	val owner: String? = null,

	@Json(name="server")
	@ColumnInfo(name = "server")
	val server: String,

	@Json(name="ispublic")
	@ColumnInfo(name = "ispublic")
	val ispublic: Int? = null,

	@Json(name="isfriend")
	@ColumnInfo(name = "isfriend")
	val isfriend: Int? = null,

	@Json(name="farm")
	@ColumnInfo(name = "farm")
	val farm: Int,

	@Json(name="id")
	@PrimaryKey()
	val id: String,

	@Json(name="secret")
	@ColumnInfo(name = "secret")
	val secret: String,

	@Json(name="title")
	@ColumnInfo(name = "title")
	val title: String? = null,

	@Json(name="isfamily")
	@ColumnInfo(name = "isfamily")
	val isfamily: Int? = null,

	@Json(name = "dateBookmarked")
	@ColumnInfo(name = "dateBookmarked")
	var dateBookmarked: Long? = null
) : Parcelable


fun PhotoItem.getPhotoUrl() =  "http://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
