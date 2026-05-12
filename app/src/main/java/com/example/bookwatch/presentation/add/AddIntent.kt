package com.example.bookwatch.presentation.add

import com.example.bookwatch.data.remote.SearchBook

sealed class AddIntent {
    data class UpdateSearchQuery(val query: String) : AddIntent()
    data class SelectBook(val book: SearchBook) : AddIntent()
    data object AddSelectedBook : AddIntent()
    data object ClearSelection : AddIntent()
    data object NavigateBack : AddIntent()
    data object NavigateToSearch : AddIntent()
}