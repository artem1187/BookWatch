package com.example.bookwatch.data.repository

import com.example.bookwatch.data.local.AppDatabase
import com.example.bookwatch.data.mapper.BookMapper
import com.example.bookwatch.data.remote.RetrofitInstance
import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.domain.model.Book
import com.example.bookwatch.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : BookRepository {

    private val bookDao = database.bookDao()

    override fun getAllBooks(): Flow<List<Book>> {
        return bookDao.getAllBooks().map { entities ->
            BookMapper.toDomainList(entities)
        }
    }

    override suspend fun insertBook(book: Book) {
        bookDao.insertBook(BookMapper.toEntity(book))
    }

    override suspend fun deleteSelectedBooks() {
        bookDao.deleteSelectedBooks()
    }

    override suspend fun updateBook(book: Book) {
        bookDao.updateBook(BookMapper.toEntity(book))
    }

    override suspend fun searchBooks(query: String): List<SearchBook> {
        return try {
            val response = RetrofitInstance.api.searchBooks(query)
            BookMapper.toSearchBookList(response.docs)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    override suspend fun getBookDescription(openLibraryKey: String): String? {
        return try {
            val details = RetrofitInstance.api.getBookDetails(openLibraryKey)

            // Описание может быть в разных форматах
            when (val desc = details.description) {
                is String -> desc
                is Map<*, *> -> desc["value"] as? String
                else -> details.firstSentence?.firstOrNull() ?: "Описание отсутствует"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}