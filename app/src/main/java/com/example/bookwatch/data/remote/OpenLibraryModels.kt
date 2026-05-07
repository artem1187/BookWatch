package com.example.bookwatch.data.remote

import com.google.gson.annotations.SerializedName

data class OpenLibrarySearchResponse(
    @SerializedName("docs")
    val docs: List<OpenLibraryDoc>,
    @SerializedName("numFound")
    val numFound: Int
)

data class OpenLibraryDoc(
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author_name")
    val authorNames: List<String>?,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,
    @SerializedName("cover_i")
    val coverId: Int?,
    @SerializedName("subject")
    val subjects: List<String>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("number_of_pages")
    val numberOfPages: Int?
)

// Упрощенная модель для поиска
data class SearchBook(
    val key: String,
    val title: String,
    val authors: String,
    val publishedDate: String,
    val thumbnailUrl: String,
    val categories: String? = null,
    val description: String? = null,
    val pageCount: Int? = null
)