package com.example.data.di

import com.example.data.repository.BookRepositoryImpl
import com.example.data.repository.LikeRepositoryImpl
import com.example.domain.repository.BookRepository
import com.example.domain.repository.LikeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

    @Binds
    @Singleton
    abstract fun bindLikeRepository(impl: LikeRepositoryImpl): LikeRepository
}