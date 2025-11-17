package com.example.domain.usecase

import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

// 좋아요 반영된 도서 검색 리스트
class LoadBooksWithLikeFlowUseCase @Inject constructor(
        private val repository: BookRepository
    ) {
        operator fun invoke(
            booksFlow: Flow<List<BookModel>>
        ): Flow<List<BookModel>> {
            return combine(booksFlow, repository.getLikesFlow()) { books, favIds ->
                books.map { it.copy(isLike = favIds.contains(it.id)) }
            }
        }
    }