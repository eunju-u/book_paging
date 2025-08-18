package com.example.presentation.ui.like

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.presentation.ButtonType
import com.example.presentation.LikeFilterType
import com.example.presentation.LikeSortType
import com.example.presentation.TabType
import com.example.presentation.ui.widget.BookItemWidget
import com.example.presentation.ui.widget.PopupWidget
import com.example.presentation.ui.widget.TopWidget
import com.example.presentation.viewModel.BookViewModel

@Composable
fun LikeContent(
    viewModel: BookViewModel,
    navController: NavController
) {
    val list by viewModel.likes.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val likeQuery by viewModel.likeQuery.collectAsState()

    var showSort by remember { mutableStateOf(false) }
    val likeSort by viewModel.likeSort.collectAsState()
    val likeFilter by viewModel.likeFilter.collectAsState()
    val buttonType by viewModel.buttonType.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLikes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopWidget(
            selectedTabType = TabType.LIKE,
            onSearch = { query ->
                viewModel.setLikeQuery(query)
            },
            text = likeQuery,
            keyboardController = keyboardController,
            selectedSort = likeSort.text,
            onSortClick = { type ->
                viewModel.selectButtonType(type)
                showSort = true
            },
            pointer = likeFilter != LikeFilterType.RESET
        )


        if (list.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
        val items: List<Any> = when (buttonType) {
            ButtonType.LIKE_SORT -> LikeSortType.entries
            ButtonType.LIKE_FILTER -> LikeFilterType.entries
            else -> emptyList()
        }

        val selectedItem = when (buttonType) {
            ButtonType.LIKE_SORT -> likeSort
            ButtonType.LIKE_FILTER -> likeFilter
            else -> null
        }

        PopupWidget(
            items = items,
            selectedItem = selectedItem,
            getText = {
                when (it) {
                    is LikeSortType -> it.text
                    is LikeFilterType -> it.text
                    else -> it.toString()
                }
            },
            onItemSelected = { selected ->
                when (selected) {
                    is LikeSortType -> viewModel.selectLikeSort(selected)
                    is LikeFilterType -> viewModel.selectLikeFilter(selected)
                }
                showSort = false
            },
            onDismiss = { showSort = false }
        )
    }
}