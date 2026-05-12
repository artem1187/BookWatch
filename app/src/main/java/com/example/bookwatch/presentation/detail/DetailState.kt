package com.example.bookwatch.presentation.detail

import com.example.bookwatch.domain.model.Book

data class DetailState(
    val book: Book? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)