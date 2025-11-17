package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LikeEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
internal abstract class LikeDb : RoomDatabase() {
    abstract fun likeDao(): LikeDao
}