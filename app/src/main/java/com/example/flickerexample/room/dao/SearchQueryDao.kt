package com.example.flickerexample.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.flickerexample.models.photos.SearchQuery

@Dao
interface SearchQueryDao {

    @Query("SELECT * from SearchQuery ORDER BY lastUsed DESC")
    suspend fun getAll(): List<SearchQuery>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg searchQuery: SearchQuery)

}