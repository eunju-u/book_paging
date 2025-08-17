package com.example.data.repository

import com.example.data.dataSource.BookDataSource
import com.example.data.dataSource.LikeDataSource
import com.example.data.mapper.fromEntityToModel
import com.example.domain.model.BookModel
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource,
    private val likeDataSource: LikeDataSource
) : BookRepository {
    private var currentPage = 1
    private val pageSize = 20
    private var isEndReached = false

    private val _booksFlow = MutableStateFlow<List<BookModel>>(emptyList())
    override val booksFlow: StateFlow<List<BookModel>> = _booksFlow

    override suspend fun resetPaging() {
        currentPage = 1
        isEndReached = false
        _booksFlow.value = emptyList()
    }

    override suspend fun loadBooks(query: String, sort: String) {
        if (isEndReached) return

        val response = bookDataSource.getBooks(query, sort, currentPage, pageSize)
        isEndReached = response.meta.is_end

        val newBooks = response.documents.map { dto -> dto.fromEntityToModel() }
        _booksFlow.value += newBooks
        currentPage++
    }
    override fun getLikesFlow(): Flow<List<String>> = likeDataSource.getAllIdsFlow()
}