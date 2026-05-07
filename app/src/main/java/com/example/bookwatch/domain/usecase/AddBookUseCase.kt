package com.example.bookwatch.domain.usecase

import com.example.bookwatch.domain.model.Book
import com.example.bookwatch.domain.repository.BookRepository
import javax.inject.Inject

class AddBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: Book) {
        repository.insertBook(book)
    }
}