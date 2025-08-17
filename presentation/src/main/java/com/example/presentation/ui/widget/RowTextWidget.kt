package com.example.presentation.ui.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RowTextWidget(
    left: String,
    right: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = left, color = color, fontWeight = FontWeight.Bold)
        Text(text = " : ", color = color)
        Text(text = right, color = color)
    }
}