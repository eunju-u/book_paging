package com.example.api.dto

data class BookResponse(
    val documents: List<BookDto>,
    val meta: Meta
)
