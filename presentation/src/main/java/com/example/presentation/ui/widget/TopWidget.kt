package com.example.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.presentation.ButtonType
import com.example.presentation.R
import com.example.presentation.TabType

@Composable
fun TopWidget(
    selectedTabType: TabType = TabType.SEARCH,
    onSearch: (String) -> Unit = {},
    keyboardController: SoftwareKeyboardController? = null,
    selectedSort: String= "",
    onSortClick: (type: ButtonType) -> Unit,
    pointer: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //제목
        Text(
            text = if (selectedTabType == TabType.SEARCH) "검색" else "즐겨찾기",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        //검색창
        SearchWidget(onSearch = onSearch, keyboardController = keyboardController)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = selectedSort)

            if (selectedTabType == TabType.SEARCH) {
                LeftImageButton(
                    text = "정렬",
                    vectorImageId = R.drawable.icon_sort,
                    onItemClick = { onSortClick(ButtonType.SEARCH_SORT) }
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LeftImageButton(
                        text = "필터",
                        vectorImageId = R.drawable.icon_filter,
                        onItemClick = { onSortClick(ButtonType.LIKE_FILTER) },
                        pointer = pointer
                    )
                    LeftImageButton(
                        text = "정렬",
                        vectorImageId = R.drawable.icon_sort,
                        onItemClick = { onSortClick(ButtonType.LIKE_SORT) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}