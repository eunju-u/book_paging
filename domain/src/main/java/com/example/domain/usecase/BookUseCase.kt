package com.example.domain.usecase

import com.example.domain.info.ErrorType
import com.example.domain.info.UiInfo
import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class BookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    private val _booksFlow = MutableStateFlow<List<BookModel>>(emptyList())

    private var currentPage = 1
    private val pageSize = 20
    private var isEndReached = false

    fun resetPaging() {
        currentPage = 1
        isEndReached = false
        _booksFlow.value = emptyList()
    }

    suspend fun loadBooks(query: String, sort: String): UiInfo<List<BookModel>> {
        return try {
            if (isEndReached) return UiInfo.PagingEnd

            val pagingBook = repository.loadBooks(query, sort, currentPage, pageSize)

            if (pagingBook.isEnd) isEndReached = true


            if (pagingBook.books.isEmpty() && currentPage == 1) return UiInfo.NoneSearch

            val list = _booksFlow.value + pagingBook.books
            _booksFlow.value = list
            currentPage++

            UiInfo.Success(list)
        } catch (e: Exception) {
            UiInfo.Error(
                if (currentPage == 1) ErrorType.NETWORK_ERROR else ErrorType.PAGING_ERROR
            )
        }
    }

    fun getCombineFlow(): Flow<List<BookModel>> {
        return combine(_booksFlow, repository.getLikesFlow()) { books, favIds ->
            books.map { it.copy(isLike = favIds.contains(it.id)) }
        }
    }

}