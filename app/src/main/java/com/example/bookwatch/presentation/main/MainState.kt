package com.example.bookwatch.presentation.main

import com.example.bookwatch.domain.model.Book

data class MainState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteDialog: Boolean = false
)