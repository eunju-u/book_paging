package com.example.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.presentation.TabType
import com.example.presentation.ui.like.LikeContent
import com.example.presentation.ui.home.SearchContent
import com.example.presentation.viewModel.BookViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: BookViewModel,
    navController: NavHostController,
) {
    val selectedTab by viewModel.selectedTab.collectAsState()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Surface(
                shadowElevation = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.selectTab(TabType.SEARCH) }) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_search),
                            colorFilter = ColorFilter.tint(Color.Black),
                            contentDescription = "검색",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    IconButton(onClick = { viewModel.selectTab(TabType.LIKE) }) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_like),
                            contentDescription = "즐겨찾기",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                TabType.SEARCH -> SearchContent(viewModel, navController)
                TabType.LIKE -> LikeContent(viewModel, navController)
            }
        }
    }
}