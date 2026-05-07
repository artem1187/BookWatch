package com.example.bookwatch.domain.usecase

import com.example.bookwatch.domain.model.Book
import com.example.bookwatch.domain.repository.BookRepository
import javax.inject.Inject

class ToggleBookSelectionUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: Book) {
        repository.updateBook(book.copy(isSelected = !book.isSelected))
    }
}