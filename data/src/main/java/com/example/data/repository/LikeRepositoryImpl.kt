package com.example.data.repository

import com.example.data.dataSource.LikeDataSource
import com.example.data.mapper.toEntity
import com.example.data.mapper.fromEntityToModel
import com.example.domain.model.BookModel
import com.example.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val localDataSource: LikeDataSource
) : LikeRepository {

    override suspend fun toggleLike(book: BookModel) {
        val allIds = localDataSource.getAllIdsFlow().first()
        if (allIds.contains(book.id)) {
            localDataSource.removeLike(book.id)
        } else {
            localDataSource.addLike(book.toEntity().copy(isLike = true))
        }
    }

    override fun getAllLikes(): Flow<List<BookModel>> {
        return localDataSource.getAllLikes().map { list -> list.map { it.fromEntityToModel() } }
    }
}