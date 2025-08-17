package com.example.domain.usecase

import com.example.domain.model.BookModel
import com.example.domain.repository.LikeRepository
import javax.inject.Inject

class ToggleLikeUseCase @Inject constructor(
    private val repository: LikeRepository
) {
    suspend operator fun invoke(book: BookModel) = repository.toggleLike(book)
}