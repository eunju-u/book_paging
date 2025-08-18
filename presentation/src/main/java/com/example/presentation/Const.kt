package com.example.presentation

enum class TabType {
    SEARCH, LIKE
}

enum class LikeSortType(val text: String) {
    ASCENDING("오름차순(제목)"), DSCENDING("내림차순(제목)")
}

enum class SearchSortType(val text: String, val label: String) {
    ACCURACY("정확도순", "accuracy"), LATEST("발간일순", "latest")
}

enum class LikeFilterType(val text: String, val range: IntRange) {
    RESET("초기화", 0..0),
    PRICE0("1만원미만", 0..9999),
    PRICE1("1만원대", 10000..19999),
    PRICE2("2만원대", 20000..29999),
    PRICE3("3만원대", 30000..39999),
    PRICE_OVER("4만원 이상", 40000..Int.MAX_VALUE)
}

enum class ButtonType {
    SEARCH_SORT,
    LIKE_SORT,
    LIKE_FILTER
}