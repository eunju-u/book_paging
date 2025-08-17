package com.example.data.dataSource

import com.example.database.LikeDao
import com.example.database.LikeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LikeDataSource @Inject constructor(
    private val likeDao: LikeDao
) {
    suspend fun addLike(entity: LikeEntity) = likeDao.insert(entity)
    suspend fun removeLike(bookId: String) = likeDao.deleteById(bookId)
    fun getAllLikes():  Flow<List<LikeEntity>> = likeDao.getAllLikeFlow()
    fun getAllIdsFlow(): Flow<List<String>> = likeDao.getAllIdsFlow()
}