package com.example.data.dataSource

import com.example.api.ApiService
import com.example.api.dto.BookResponse
import javax.inject.Inject

class BookDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): BookResponse {
        return apiService.getBooks(query, sort, page, size)
    }
}