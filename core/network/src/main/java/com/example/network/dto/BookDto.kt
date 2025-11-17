package com.example.network.dto

import androidx.annotation.Keep

@Keep
data class BookDto(
    val id: Int,
    val title: String, //도서 제목
    val contents: String, //도서 소개
    val url: String, //도서 상세 URL
    val isbn: String, //도서서 상세 URL
    val datetime: String, //도서 출판날
    val authors: List<String>, //도서 저자 리스트
    val publisher: String, //도서 출판사
    val translators: List<String>, //도서 번역자 리스트
    val price: Int, //도서 정가
    val sale_price: Int, //sale_price
    val thumbnail: String, //도서 표지 미리보기 URL
    val status: String, //도서 판매 상태 정보
)