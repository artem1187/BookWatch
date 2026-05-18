package com.example.bookwatch.domain.repository

import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBooks(): Flow<List<Book>>
    suspend fun insertBook(book: Book)
    suspend fun deleteSelectedBooks()
    suspend fun updateBook(book: Book)
    suspend fun searchBooks(query: String): List<SearchBook>
    suspend fun getBookDescription(openLibraryKey: String): String?
}