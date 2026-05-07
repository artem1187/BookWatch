package com.example.bookwatch.domain.usecase

import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.domain.repository.BookRepository
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(openLibraryKey: String): SearchBook? {
        return repository.getBookDetails(openLibraryKey)
    }
}