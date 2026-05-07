package com.example.bookwatch.domain.usecase

import com.example.bookwatch.domain.model.Book
import com.example.bookwatch.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<List<Book>> = repository.getAllBooks()
}