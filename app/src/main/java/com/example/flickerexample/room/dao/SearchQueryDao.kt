package com.example.flickerexample.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.flickerexample.models.SearchQuery

@Dao
interface SearchQueryDao {

    @Query("SELECT * from SearchQuery")
    suspend fun getAll(): List<SearchQuery>

    @Insert(onConflict = REPLACE)
    suspend fun insert(searchQuery: SearchQuery)
}