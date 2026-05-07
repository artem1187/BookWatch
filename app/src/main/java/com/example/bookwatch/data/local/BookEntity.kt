package com.example.bookwatch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val authors: String,
    val publishedDate: String,
    val thumbnailUrl: String,
    val categories: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val isSelected: Boolean = false,
    val openLibraryKey: String? = null
)