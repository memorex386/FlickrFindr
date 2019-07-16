package com.example.flickerexample.models.photos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant


@Entity(tableName = "SearchQuery")
data class SearchQuery(
    @PrimaryKey() var searchTerm: String,
    @ColumnInfo(name = "lastUsed") var lastUsed: Long = Instant.now().toEpochMilli()
)