package com.example.presentation

enum class TabType {
    SEARCH, LIKE
}

enum class SearchSortType(val text: String, val label: String) {
    ACCURACY("정확도순", "accuracy"), LATEST("발간일순", "latest")
}