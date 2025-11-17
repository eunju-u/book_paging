package com.example.domain.repository

import com.example.domain.model.PageBooks
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun loadBooks(query: String, sort: String, page: Int, size: Int): PageBooks
    fun getLikesFlow(): Flow<List<String>>
}