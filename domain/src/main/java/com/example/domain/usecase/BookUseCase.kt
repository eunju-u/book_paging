package com.example.domain.usecase

import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class BookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend fun loadBooks(query: String, sort: String) = repository.loadBooks(query, sort)

    fun getCombineFlow(): Flow<List<BookModel>> {
        return combine(repository.booksFlow, repository.getLikesFlow()) { books, favIds ->
            books.map { it.copy(isLike = favIds.contains(it.id)) }
        }
    }

    suspend fun resetPaging() = repository.resetPaging()
}