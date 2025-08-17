package com.example.domain.repository

import com.example.domain.model.BookModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BookRepository {
    suspend fun loadBooks(query: String, sort: String)
    suspend fun resetPaging()
    val booksFlow: StateFlow<List<BookModel>>
    fun getLikesFlow(): Flow<List<String>>
}