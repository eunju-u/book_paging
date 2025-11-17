package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
 interface LikeDao {
    @Query("SELECT * FROM like_table")
    fun getAllLikeFlow(): Flow<List<LikeEntity>>

    @Query("SELECT id FROM like_table")
    fun getAllIdsFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LikeEntity)

    @Query("DELETE FROM like_table WHERE id = :bookId")
    suspend fun deleteById(bookId: String)
}