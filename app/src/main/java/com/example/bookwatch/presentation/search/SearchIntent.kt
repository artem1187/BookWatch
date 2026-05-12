package com.example.bookwatch.presentation.search

import com.example.bookwatch.data.remote.SearchBook

sealed class SearchIntent {
    data class Search(val query: String) : SearchIntent()
    data class SelectBook(val book: SearchBook) : SearchIntent()
    data object ClearResults : SearchIntent()
    data object NavigateBack : SearchIntent()
}