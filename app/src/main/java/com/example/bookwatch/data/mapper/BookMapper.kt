package com.example.bookwatch.data.mapper

import com.example.bookwatch.data.local.BookEntity
import com.example.bookwatch.data.remote.OpenLibraryDoc
import com.example.bookwatch.data.remote.SearchBook
import com.example.bookwatch.domain.model.Book

object BookMapper {

    fun toDomain(entity: BookEntity): Book {
        return Book(
            id = entity.id,
            title = entity.title,
            authors = entity.authors,
            publishedDate = entity.publishedDate,
            thumbnailUrl = entity.thumbnailUrl,
            categories = entity.categories,
            description = entity.description,
            pageCount = entity.pageCount,
            isSelected = entity.isSelected,
            openLibraryKey = entity.openLibraryKey
        )
    }

    fun toEntity(domain: Book): BookEntity {
        return BookEntity(
            id = domain.id,
            title = domain.title,
            authors = domain.authors,
            publishedDate = domain.publishedDate,
            thumbnailUrl = domain.thumbnailUrl,
            categories = domain.categories,
            description = domain.description,
            pageCount = domain.pageCount,
            isSelected = domain.isSelected,
            openLibraryKey = domain.openLibraryKey
        )
    }

    fun toDomainList(entities: List<BookEntity>): List<Book> {
        return entities.map { toDomain(it) }
    }

    fun toSearchBook(doc: OpenLibraryDoc): SearchBook {
        // Формируем URL обложки
        val coverUrl = doc.coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" } ?: ""

        return SearchBook(
            key = doc.key,
            title = doc.title,
            authors = doc.authorNames?.joinToString(", ") ?: "Unknown Author",
            publishedDate = doc.firstPublishYear?.toString() ?: "Unknown",
            thumbnailUrl = coverUrl,
            categories = doc.subjects?.firstOrNull(),
            description = doc.description,
            pageCount = doc.numberOfPages
        )
    }

    fun toSearchBookList(docs: List<OpenLibraryDoc>): List<SearchBook> {
        return docs.map { toSearchBook(it) }
    }
}