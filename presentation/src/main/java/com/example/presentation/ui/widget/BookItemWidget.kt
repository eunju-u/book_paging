package com.example.presentation.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.domain.model.BookModel
import com.example.presentation.R

@Composable
fun BookItemWidget(
    modifier: Modifier = Modifier,
    book: BookModel,
    onHeartClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(book.thumbnail)
            .crossfade(true)
            .error(R.drawable.img_error)
            .build()
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(12.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 이미지
            Image(
                painter = painter,
                contentDescription = "책 이미지",
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 이미지 옆 제목/지은이
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "도서",
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray),
                )
                Text(text = book.title, fontWeight = FontWeight.Bold)
                RowTextWidget(
                    left = "저자", right = book.authors.joinToString(", "), color = Color.Gray
                )
                RowTextWidget(left = "출판사", right = book.publisher, color = Color.Gray)
                RowTextWidget(left = "출간일", right = book.datetime, color = Color.Gray)
                RowTextWidget(left = "가격", right = "${book.price}원", color = Color.Gray)

            }
        }

        IconButton(
            onClick = onHeartClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                painter = painterResource(
                    id = if (book.isLike) R.drawable.icon_heart_full else R.drawable.icon_heart
                ),
                contentDescription = "즐겨찾기",
                tint = if (book.isLike) Color.Red else Color.Gray
            )
        }

        // 오른쪽 하단 숫자
        Text(
            text = "${book.price}원",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
