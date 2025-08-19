package com.example.domain.info

enum class ErrorType {
    PAGING_ERROR,
    NETWORK_ERROR
}

sealed class UiInfo<out T> {
    data class Success<out T>(val data: T?) : UiInfo<T>()
    data class Error(val errorType: ErrorType) : UiInfo<Nothing>()
    data object PagingEnd : UiInfo<Nothing>()
    data object NoneSearch : UiInfo<Nothing>()
}