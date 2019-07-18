package com.example.flickerexample.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flickerexample.core.app
import com.example.flickerexample.models.photos.PhotoItem
import com.example.flickerexample.models.photos.SearchQuery
import com.example.flickerexample.room.dao.PhotoItemDao
import com.example.flickerexample.room.dao.SearchQueryDao

@Database(entities = [SearchQuery::class, PhotoItem::class], version = 1)
abstract class _FlickrDB : RoomDatabase() {

    abstract fun searchQueryDao(): SearchQueryDao

    abstract fun photoItemDao(): PhotoItemDao

}

val flickerDB by lazy {
    synchronized(_FlickrDB::class) {
        Room.databaseBuilder(
            app,
            _FlickrDB::class.java, "flickr.db"
        ).build()
    }
}

