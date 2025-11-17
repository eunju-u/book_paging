package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.LikeDao
import com.example.database.LikeDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LikeDb =
        Room.databaseBuilder(context, LikeDb::class.java, "like_database").build()

    @Provides
    @Singleton
    fun provideLikeDao(db: LikeDb): LikeDao = db.likeDao()
}