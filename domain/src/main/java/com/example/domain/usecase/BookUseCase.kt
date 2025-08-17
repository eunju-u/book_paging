package com.example.domain.usecase

import com.example.domain.repository.BookRepository
import javax.inject.Inject

class BookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend fun loadBooks(query: String, sort: String) = repository.loadBooks(query, sort)
    suspend fun resetPaging() = repository.resetPaging()
}