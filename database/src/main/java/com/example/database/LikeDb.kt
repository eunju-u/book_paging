package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LikeEntity::class], version = 1, exportSchema = false)
abstract class LikeDb : RoomDatabase() {
    abstract fun likeDao(): LikeDao
}