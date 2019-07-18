package com.example.flickerexample.room.dao

import androidx.room.*
import com.example.flickerexample.models.photos.PhotoItem

@Dao
interface PhotoItemDao {

    @Query("SELECT * from PhotoItem ORDER BY dateBookmarked DESC")
    suspend fun getAll(): List<PhotoItem>

    @Query("SELECT * FROM PhotoItem WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PhotoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg photoItem: PhotoItem)

    @Delete
    suspend fun delete(vararg photoItem: PhotoItem)

}