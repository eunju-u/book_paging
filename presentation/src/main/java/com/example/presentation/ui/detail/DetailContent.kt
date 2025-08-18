package com.example.presentation.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.presentation.R
import com.example.presentation.ui.widget.RowTextWidget
import com.example.presentation.viewModel.BookViewModel

@Composable
fun DetailContent(
    viewModel: BookViewModel,
    bookId: String,
    onFinish: () -> Unit = {},
) {
    val context = LocalContext.current
    val currentBook by viewModel.getBookFlow(bookId).collectAsState(initial = null)

    if (currentBook == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val book = currentBook!!

    var isLike by remember { mutableStateOf(book.isLike) }
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(book.thumbnail)
            .crossfade(true)
            .error(R.drawable.img_error)
            .build()
    )

    DisposableEffect(key1 = bookId) {
        onDispose {
            if (isLike != book.isLike) {
                viewModel.toggleLike(book.copy(isLike = isLike))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onFinish) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.icon_back),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = { isLike = !isLike },
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(
                        id = if (isLike) R.drawable.icon_heart_full else R.drawable.icon_heart
                    ),
                    contentDescription = "즐겨찾기",
                    tint = if (isLike) Color.Red else Color.Gray
                )
            }
        }

        Text(
            text = book.title,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            // 왼쪽 이미지
            Image(
                painter = painter,
                contentDescription = "책 이미지",
                modifier = Modifier
                    .width(170.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 이미지 옆 제목/지은이
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                RowTextWidget(
                    left = stringResource(id = R.string.book_author),
                    right = book.authors.joinToString(", "),
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = R.string.book_publisher),
                    right = book.publisher,
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = R.string.book_date),
                    right = book.datetime,
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = R.string.book_isbm),
                    right = book.isbn,
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = R.string.book_price),
                    right = "${book.price}원",
                    color = Color.Black
                )
                RowTextWidget(
                    left = stringResource(id = R.string.book_sale_price),
                    right = "${book.salePrice}원",
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(id = R.string.book_info),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = book.contents, color = Color.Black)
    }
}