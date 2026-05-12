package com.example.bookwatch.presentation.search

import com.example.bookwatch.data.remote.SearchBook

data class SearchState(
    val searchResults: List<SearchBook> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = ""
)