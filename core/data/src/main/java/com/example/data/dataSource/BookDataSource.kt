package com.example.data.dataSource

import com.example.network.ApiService
import com.example.network.dto.BookResponse
import javax.inject.Inject

internal class BookDataSource @Inject constructor(
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