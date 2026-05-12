package com.example.bookwatch.presentation.search

import com.example.bookwatch.data.remote.SearchBook

sealed class SearchEffect {
    data class BookSelected(val book: SearchBook) : SearchEffect()
    data object NavigateBack : SearchEffect()
    data class ShowError(val message: String) : SearchEffect()
}