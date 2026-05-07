package com.example.bookwatch.domain.usecase

import com.example.bookwatch.domain.repository.BookRepository
import javax.inject.Inject

class DeleteSelectedBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke() {
        repository.deleteSelectedBooks()
    }
}