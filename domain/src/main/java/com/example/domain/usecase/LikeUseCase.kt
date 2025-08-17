package com.example.domain.usecase

import com.example.domain.model.BookModel
import com.example.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LikeUseCase @Inject constructor(
    private val repository: LikeRepository
) {
    operator fun invoke(): Flow<List<BookModel>> = repository.getAllLikes()
}