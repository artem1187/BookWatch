package com.example.bookwatch.presentation.add

import com.example.bookwatch.data.remote.SearchBook

data class AddState(
    val searchQuery: String = "",
    val selectedBook: SearchBook? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)