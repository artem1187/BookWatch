package com.example.bookwatch.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApiService {

    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): OpenLibrarySearchResponse


    @GET("{key}.json")
    suspend fun getBookDetails(
        @Path("key") key: String
    ): OpenLibraryDoc
}