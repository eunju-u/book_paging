package com.example.network

import com.example.network.dto.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v3/search/book")
    suspend fun getBooks(
        @Query("query") query: String, //검색어
        @Query("sort") sort: String, //정렬 기준
        @Query("page") page: Int, //페이징 페이지
        @Query("size") size: Int, //페이지당 항목 수
    ): BookResponse
}