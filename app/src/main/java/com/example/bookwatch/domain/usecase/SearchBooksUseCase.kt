package com.example.bookwatch.domain.usecase

import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.domain.repository.BookRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(query: String): List<SearchBook> {
        return repository.searchBooks(query)
    }
}