package com.route.e_commercec43gsunwed.utils.pager

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.route.domain.model.products.ProductDetailsData
import com.route.e_commercec43gsunwed.R


@Composable
fun ECommerceHorizontalPager(modifier: Modifier = Modifier) {
    val state = rememberPagerState(pageCount = { 3 })
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.height(200.dp)) {
        HorizontalPager(
            state = state, modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(200.dp)
                .fillMaxWidth()
        ) {
            for (i in 0 until state.pageCount) {
                Box(
                    Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.slider_image_1),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Text(
                        "Up TO 25%", modifier = Modifier.align(Alignment.CenterStart),
                        color = colorScheme.secondary
                    )
                }
            }

        }
        // Slider Dots
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until state.pageCount)
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 4.dp)
                        .size(10.dp)
                        .background(
                            color = if (state.currentPage == i) colorScheme.secondary else colorScheme.onSecondary,
                            CircleShape
                        )
                )
        }
    }
}

@Composable
fun ProductDetailImagesPager(modifier: Modifier = Modifier, productDetails: ProductDetailsData?) {
    Log.e("TAG", "ProductDetailImagesPager: ${productDetails?.images}")
    val state = remember(productDetails?.images?.size) {
        PagerState(
            currentPage = 0,
            pageCount = { productDetails?.images?.size ?: 0 }
        )
    }
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.height(300.dp)) {
        HorizontalPager(
            state = state, modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(300.dp)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colorScheme.onSecondary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                AsyncImage(
                    model = productDetails?.images?.get(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }
        // Slider Dots
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until state.pageCount)
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 4.dp)
                        .height(7.dp)
                        .width(if (state.currentPage == i) 30.dp else 7.dp)
                        .background(
                            color = if (state.currentPage == i) colorScheme.secondary else colorScheme.onSecondary,
                            if (state.currentPage == i) RoundedCornerShape(30.dp) else CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = colorScheme.secondary,
                            shape = if (state.currentPage == i) RoundedCornerShape(30.dp) else CircleShape
                        )

                )
        }
    }
}
