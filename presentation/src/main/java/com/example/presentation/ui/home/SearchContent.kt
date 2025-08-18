package com.example.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.presentation.SearchSortType
import com.example.presentation.TabType
import com.example.presentation.ui.widget.BookItemWidget
import com.example.presentation.ui.widget.PopupWidget
import com.example.presentation.ui.widget.TopWidget
import com.example.presentation.viewModel.BookViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchContent(
    viewModel: BookViewModel,
    navController: NavController
) {
    val list by viewModel.books.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showSort by remember { mutableStateOf(false) }
    val selectedSort by viewModel.searchSort.collectAsState()
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 페이징 처리
    LaunchedEffect(state, list) {
        snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (list.isNotEmpty() && lastIndex == list.lastIndex) {
                    viewModel.loadBooks()
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collect {
            coroutineScope.launch {
                state.scrollToItem(0)
            }
        }
    }

    Box(Modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopWidget(
                selectedTabType = TabType.SEARCH,
                onSearch = { query ->
                    viewModel.setSearchQuery(query)
                },
                text = searchQuery,
                keyboardController = keyboardController,
                selectedSort = selectedSort.text,
                onSortClick = { type ->
                    viewModel.selectButtonType(type)
                    showSort = true
                }
            )


            if (list.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = state,
                ) {
                    itemsIndexed(items = list) { _, item ->
                        BookItemWidget(
                            book = item,
                            onHeartClick = { viewModel.toggleLike(item) },
                            onClick = {
                                navController.navigate("DetailContent/${item.id}")
                            }
                        )
                    }
                }
            }
        }

        if (showSort) {
            PopupWidget(
                items = SearchSortType.entries,
                selectedItem = selectedSort,
                onDismiss = { showSort = false },
                onItemSelected = {
                    viewModel.selectSearchSort(it)
                    showSort = false
                }, getText = { it.text }
            )
        }
        if (viewModel.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.Center)
            )
        }
    }
}