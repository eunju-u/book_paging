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
import com.example.presentation.TabType
import com.example.presentation.ui.widget.BookItemWidget
import com.example.presentation.ui.widget.TopWidget
import com.example.presentation.viewModel.BookViewModel

@Composable
fun LikeContent(
    viewModel: BookViewModel
) {
    val list by viewModel.likes.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var showSort by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadLikes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopWidget(
            selectedTabType = TabType.LIKE,
            onSearch = { query ->
                viewModel.loadLikes(query)
            },
            keyboardController = keyboardController,
            onSortClick = { showSort = true }
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
                        onClick = {}
                    )
                }
            }
        }
    }

    if (showSort) {
        //TODO 핍압 노출
    }
}