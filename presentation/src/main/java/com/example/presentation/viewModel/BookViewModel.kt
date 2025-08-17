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
import com.example.presentation.SearchSortType
import com.example.presentation.TabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    var searchQuery = ""
    var likeQuery = ""

    // 검색탭 정렬 선택값
    private val _searchSort = MutableStateFlow(SearchSortType.ACCURACY)
    val searchSort: StateFlow<SearchSortType> = _searchSort

    private val _scrollToTop = MutableSharedFlow<Unit>()
    val scrollToTop = _scrollToTop.asSharedFlow()

    // 바텀 탭 선택
    fun selectTab(tab: TabType) {
        _selectedTab.value = tab
    }

    //데이터 로드 및 페이징 처리
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

    fun loadLikes(query: String = likeQuery) = viewModelScope.launch {
        likeQuery = query
        getLikeUseCase().collect { allLikes ->
            _likes.value = allLikes
        }
    }

    fun toggleLike(book: BookModel) {
        viewModelScope.launch {
            val toggledBook = book.copy(isLike = !book.isLike)
            toggleLikeUseCase(toggledBook)
            _likes.update { list -> list.map { if (it.id == book.id) toggledBook else it } }

        }
    }
}