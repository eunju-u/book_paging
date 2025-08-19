package com.example.data.repository

import com.example.data.dataSource.BookDataSource
import com.example.data.dataSource.LikeDataSource
import com.example.data.mapper.fromEntityToModel
import com.example.domain.model.PageBooks
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource,
    private val likeDataSource: LikeDataSource
) : BookRepository {

    override suspend fun loadBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): PageBooks {
        val response = bookDataSource.getBooks(query, sort, page, size)

        val books = response.documents.map { it.fromEntityToModel() }
        val isEnd = response.meta.is_end

        return PageBooks(
            books = books,
            isEnd = isEnd
        )
    }

    override fun getLikesFlow(): Flow<List<String>> = likeDataSource.getAllIdsFlow()
}