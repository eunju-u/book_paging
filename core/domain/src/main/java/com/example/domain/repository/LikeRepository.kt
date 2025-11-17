package com.example.domain.repository

import com.example.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface LikeRepository {
    suspend fun toggleLike(book: BookModel)
    fun getAllLikes(): Flow<List<BookModel>>
}