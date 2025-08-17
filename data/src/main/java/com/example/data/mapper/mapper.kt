package com.example.data.mapper

import com.example.api.dto.BookDto
import com.example.database.LikeEntity
import com.example.domain.model.BookModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun String.toFormattedDate(): String {
    return try {
        val parsed = OffsetDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        parsed.format(DateTimeFormatter.ISO_LOCAL_DATE) // yyyy-MM-dd
    } catch (e: Exception) {
        this
    }
}

fun BookDto.fromEntityToModel(isLike: Boolean = false): BookModel {

    return BookModel(
        id = isbn.replace(" ", ""),
        title = title,
        contents = contents,
        url = url,
        isbn = isbn,
        datetime = datetime.toFormattedDate(),
        authors = authors,
        publisher = publisher,
        translators = translators,
        price = price,
        salePrice = sale_price,
        thumbnail = thumbnail,
        status = status,
        isLike = isLike
    )
}

fun BookModel.toEntity(): LikeEntity = LikeEntity(
    id,
    title,
    contents,
    url,
    isbn,
    datetime,
    authors,
    publisher,
    translators,
    price,
    salePrice,
    thumbnail,
    status,
    isLike
)

fun LikeEntity.fromEntityToModel(): BookModel = BookModel(
    id,
    title,
    contents,
    url,
    isbn,
    datetime,
    authors,
    publisher,
    translators,
    price,
    sale_price,
    thumbnail,
    status,
    isLike
)
