package com.example.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookModel
import com.example.domain.usecase.BookUseCase
import com.example.domain.usecase.LikeUseCase
import com.example.domain.usecase.ToggleLikeUseCase
import com.example.presentation.ButtonType
import com.example.presentation.LikeFilterType
import com.example.presentation.LikeSortType
import com.example.presentation.SearchSortType
import com.example.presentation.TabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookUseCase: BookUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val getLikeUseCase: LikeUseCase,
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(TabType.SEARCH)
    val selectedTab: StateFlow<TabType> = _selectedTab

    //검색리스트
    val books =
        bookUseCase.getCombineFlow().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    var isLoading by mutableStateOf(false)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _likeQuery = MutableStateFlow("")
    val likeQuery: StateFlow<String> = _likeQuery

    // 버튼 타입
    private val _buttonType = MutableStateFlow(ButtonType.SEARCH_SORT)
    val buttonType: StateFlow<ButtonType> = _buttonType

    // 검색탭 정렬 선택값
    private val _searchSort = MutableStateFlow(SearchSortType.ACCURACY)
    val searchSort: StateFlow<SearchSortType> = _searchSort

    // 즐겨찾기 정렬 선택
    private val _likeSort = MutableStateFlow(LikeSortType.ASCENDING)
    val likeSort: StateFlow<LikeSortType> = _likeSort

    // 즐겨찾기 필터 선택값
    private val _likeFilter = MutableStateFlow(LikeFilterType.RESET)
    val likeFilter: StateFlow<LikeFilterType?> = _likeFilter

    private val _scrollToTop = MutableSharedFlow<Unit>()
    val scrollToTop = _scrollToTop.asSharedFlow()

    // 즐겨찾기 리스트
    val likes: StateFlow<List<BookModel>> =
        combine(
            getLikeUseCase(),
            _likeSort,
            _likeFilter,
            _likeQuery
        ) { allList, sort, filter, query ->
            var filtered = allList

            if (filter != LikeFilterType.RESET) {
                filtered = filtered.filter { it.price in filter.range }
            }

            if (query.isNotBlank()) {
                filtered = filtered.filter { it.title.contains(query, ignoreCase = true) }
            }

            when (sort) {
                LikeSortType.ASCENDING -> filtered.sortedBy { it.title }
                LikeSortType.DSCENDING -> filtered.sortedByDescending { it.title }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    init {
        viewModelScope.launch {
            _searchQuery
                .collectLatest { query ->
                    loadBooks(query, forceReload = true)
                }
        }
    }

    // 바텀 탭 선택
    fun selectTab(tab: TabType) {
        _selectedTab.value = tab
    }

    // 버튼 타입 선택
    fun selectButtonType(type: ButtonType) {
        _buttonType.value = type
    }

    // search - 정렬 타입 선택
    fun selectSearchSort(sort: SearchSortType) {
        _searchSort.value = sort
        loadBooks(forceReload = true)
    }

    // like - 정렬 타입 선택
    fun selectLikeSort(sort: LikeSortType) {
        _likeSort.value = sort
    }

    // like - 필터 타입 선택
    fun selectLikeFilter(filterType: LikeFilterType) {
        _likeFilter.value = filterType
    }

    // 검색 데이터 로드 및 페이징 처리
    fun loadBooks(
        query: String = searchQuery.value,
        sort: SearchSortType = searchSort.value,
        forceReload: Boolean = false
    ) {
        viewModelScope.launch {
            isLoading = true
            if (forceReload) {
                bookUseCase.resetPaging()
                _scrollToTop.emit(Unit)
            }
            bookUseCase.loadBooks(query, sort.label)
            isLoading = false
        }
    }

    // like DB 데이터 로드
    fun loadLikes(query: String = likeQuery.value) = viewModelScope.launch {
        _likeQuery.value = query
    }

    fun toggleLike(book: BookModel) {
        viewModelScope.launch {
            val toggledBook = book.copy(isLike = !book.isLike)
            toggleLikeUseCase(toggledBook)
        }
    }

    // book id로 책 정보 get
    fun getBookFlow(bookId: String): Flow<BookModel?> {
        return combine(books, likes) { bookList, likeList ->
            bookList.firstOrNull { it.id == bookId } ?: likeList.firstOrNull { it.id == bookId }
        }
    }

    // book 검색 쿼리 설정
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // like 검색 쿼리 설정
    fun setLikeQuery(query: String) {
        _likeQuery.value = query
    }
}