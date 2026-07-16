package com.route.e_commercec43gsunwed.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.route.domain.model.categories.CategoryItem
import com.route.domain.model.products.ProductItem
import com.route.e_commercec43gsunwed.R

@Composable
fun CategoryCard(modifier: Modifier = Modifier, category: CategoryItem) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier
            .height(110.dp)
            .width(100.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        AsyncImage(
            model = category.image,
            contentDescription = category.name,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = category.name ?: "", color = colorScheme.onBackground, fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

    }
}

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: ProductItem,
    isInWishlist: Boolean,
    onProductClick: (ProductItem) -> Unit,
    onAddCartClick: (ProductItem) -> Unit,
    onAddWishlistClick: (ProductItem) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier
            .width(160.dp),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, colorScheme.primaryContainer),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onSecondary,
            contentColor = colorScheme.onBackground
        ),
        onClick = {
            onProductClick(product)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            contentAlignment = Alignment.TopEnd
        ) {

            AsyncImage(
                product.imageCover ?: "",
                contentDescription = product.description,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = if (isInWishlist) painterResource(R.drawable.ic_wishlist_filled) else painterResource(
                    R.drawable.ic_add_wishlist
                ),
                contentDescription = stringResource(R.string.add_to_wishlist_image),
                modifier = Modifier
                    .clickable(true) {
                        onAddWishlistClick(product)
                    }
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .background(colorScheme.onSecondary, CircleShape)
                    .padding(3.dp),

                )
        }
        Text(
            product.brand?.name ?: "",
            color = colorScheme.onBackground,
            minLines = 1,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            product.title ?: "",
            color = colorScheme.onBackground,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            "${product.price} ${stringResource(R.string.egp)}",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.review),
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.W400,
                fontSize = 12.sp
            )
            Text(
                "( ${product.ratingsAverage} )", fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            Image(
                painter = painterResource(R.drawable.ic_rating),
                contentDescription = stringResource(
                    R.string.rating
                )
            )
            Spacer(Modifier.weight(1F))
            Image(
                painter = painterResource(R.drawable.ic_add_cart),
                contentDescription = stringResource(
                    R.string.icon_add_cart
                ),
                modifier = Modifier
                    .clickable(true) {
                        onAddCartClick(product)
                    }
                    .padding(end = 8.dp)
            )
        }
    }
}
