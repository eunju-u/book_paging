package com.example.network.dto

data class BookResponse(
    val documents: List<BookDto>,
    val meta: Meta
)
