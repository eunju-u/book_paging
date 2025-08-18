package com.example.book_paging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.book_paging.ui.theme.Book_pagingTheme
import com.example.presentation.ui.MainScreen
import com.example.presentation.ui.detail.DetailContent
import com.example.presentation.viewModel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Book_pagingTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 메인 화면
                        composable("main") {
                            MainScreen(modifier = Modifier.fillMaxSize(), viewModel, navController)
                        }
                        // 상세 화면
                        composable(
                            route = "DetailContent/{bookId}",
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val bookId =
                                backStackEntry.arguments?.getString("bookId") ?: return@composable
                            DetailContent(
                                viewModel = viewModel,
                                bookId = bookId,
                                onFinish = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
