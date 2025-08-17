package com.example.domain.repository

interface BookRepository {
    suspend fun loadBooks(query: String, sort: String)
    suspend fun resetPaging()
}