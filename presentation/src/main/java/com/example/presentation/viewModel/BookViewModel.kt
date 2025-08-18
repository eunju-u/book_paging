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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    val books = bookUseCase.getCombineFlow().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _likes = MutableStateFlow<List<BookModel>>(emptyList())

    val likes: StateFlow<List<BookModel>> = _likes

    var isLoading by mutableStateOf(false)

    private var searchQuery = ""
    private var likeQuery = ""

    // 버튼 타입
    private val _buttonType = MutableStateFlow(ButtonType.SEARCH_SORT)
    val buttonType: StateFlow<ButtonType> = _buttonType

    // 검색탭 정렬 선택값
    private val _searchSort = MutableStateFlow(SearchSortType.ACCURACY)
    val searchSort: StateFlow<SearchSortType> = _searchSort

    // 즐겨찾기 정렬 선택
    private val _likeSort = MutableStateFlow(LikeSortType.DSCENDING)
    val likeSort: StateFlow<LikeSortType> = _likeSort

    // 즐겨찾기 필터 선택값
    private val _likeFilter = MutableStateFlow(LikeFilterType.RESET)
    val likeFilter: StateFlow<LikeFilterType?> = _likeFilter

    private val _scrollToTop = MutableSharedFlow<Unit>()
    val scrollToTop = _scrollToTop.asSharedFlow()

    init {
        viewModelScope.launch {
            getLikeUseCase().collect { allLikes ->
                applyLikesFilter(allLikes)
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
        refreshLikes()
    }

    // like - 필터 타입 선택
    fun selectLikeFilter(filterType: LikeFilterType) {
        _likeFilter.value = filterType
        refreshLikes()
    }

    // 검색 데이터 로드 및 페이징 처리
    fun loadBooks(
        query: String = searchQuery,
        sort: SearchSortType = searchSort.value,
        forceReload: Boolean = false
    ) {
        viewModelScope.launch {
            isLoading = true
            if (forceReload || query != searchQuery) {
                bookUseCase.resetPaging()
                _scrollToTop.emit(Unit)
                searchQuery = query
            }
            bookUseCase.loadBooks(query, sort.label)
            isLoading = false
        }
    }

    // like DB 데이터 로드
    fun loadLikes(query: String = likeQuery) = viewModelScope.launch {
        likeQuery = query
        refreshLikes()
    }

    // like 데이터 갱신
    private fun refreshLikes() {
        viewModelScope.launch {
            val current = getLikeUseCase().first()
            applyLikesFilter(current)
        }
    }

    fun toggleLike(book: BookModel) {
        viewModelScope.launch {
            val toggledBook = book.copy(isLike = !book.isLike)
            toggleLikeUseCase(toggledBook)
            _likes.update { list -> list.map { if (it.id == book.id) toggledBook else it } }

        }
    }

    // like 데이터 필터, 검색, 정렬 적용
    private fun applyLikesFilter(allLikes: List<BookModel>) {
        val filtered = if (_likeFilter.value != LikeFilterType.RESET) {
            allLikes.filter { it.price in _likeFilter.value.range }
        } else allLikes

        val searched = if (likeQuery.isNotBlank()) {
            filtered.filter { it.title.contains(likeQuery, ignoreCase = true) }
        } else filtered

        val sorted = when (_likeSort.value) {
            LikeSortType.ASCENDING -> searched.sortedBy { it.title }
            LikeSortType.DSCENDING -> searched.sortedByDescending { it.title }
        }

        _likes.value = sorted
    }
}