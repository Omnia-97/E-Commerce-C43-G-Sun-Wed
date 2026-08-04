package com.route.e_commercec43gsunwed.screens.home.composable.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.route.domain.model.Result
import com.route.domain.model.wishlist.WishlistItem
import com.route.e_commercec43gsunwed.LocalNavController
import com.route.e_commercec43gsunwed.R
import com.route.e_commercec43gsunwed.destinations.AppRoutes
import com.route.e_commercec43gsunwed.utils.ECommerceSearchAppBar
import com.route.e_commercec43gsunwed.utils.ErrorDialog

@Composable
fun WishlistTab(modifier: Modifier = Modifier) {
    val viewModel: WishlistViewModel = hiltViewModel()
    val state = viewModel.states.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    val navController = LocalNavController.current
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.handleActions(WishlistContract.Actions.GetWishlist)
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is WishlistContract.Events.NavigateToProductDetails -> {
                    navController.navigate(AppRoutes.ProductDetailsDestination(it.productId))
                }

                is WishlistContract.Events.ShowMessage -> errorMessage = it.message
                WishlistContract.Events.Idle -> {}
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(colorScheme.onSecondary)
    ) {
        ECommerceSearchAppBar(
            onCartClick = {
                navController.navigate(AppRoutes.CartDestination)
            },
            onSearchClick = {},
            cartItemsCount = state.value.cartItemsCount
        )

        when (val wishlistState = state.value.wishlist) {
            null -> {
                if (state.value.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorScheme.secondary)
                    }
                }
            }

            is Result.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = wishlistState.failure.message
                            ?: stringResource(R.string.something_went_wrong),
                        color = colorScheme.tertiary
                    )
                }
            }

            is Result.Success -> {
                val items = wishlistState.data ?: emptyList()
                if (items.isEmpty()) {
                    WishlistEmptyState()
                } else {
                    WishlistLazyColumn(
                        items = items,
                        removingProductId = state.value.removingProductId,
                        onRemoveFromWishlist = { productId ->
                            viewModel.handleActions(
                                WishlistContract.Actions.RemoveFromWishlist(productId)
                            )
                        },
                        onAddToCart = { productId ->
                            viewModel.handleActions(
                                WishlistContract.Actions.AddToCart(productId)
                            )
                        },
                        onItemClick = { productId ->
                            viewModel.handleActions(
                                WishlistContract.Actions.ClickedOnProduct(productId)
                            )
                        }
                    )
                }
            }
        }
    }

    if (errorMessage != null) {
        ErrorDialog(errorState = errorMessage) {
            errorMessage = null
        }
    }
}

@Composable
fun WishlistLazyColumn(
    modifier: Modifier = Modifier,
    items: List<WishlistItem>,
    removingProductId: String?,
    onRemoveFromWishlist: (String) -> Unit,
    onAddToCart: (String) -> Unit,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items, key = { it.id ?: it.hashCode().toString() }) { item ->
            WishlistItemRow(
                item = item,
                isRemoving = removingProductId == item.id,
                onRemoveFromWishlist = { item.id?.let { onRemoveFromWishlist(it) } },
                onAddToCart = { item.id?.let { onAddToCart(it) } },
                onItemClick = { item.id?.let { onItemClick(it) } }
            )
        }
    }
}

@Composable
fun WishlistItemRow(
    modifier: Modifier = Modifier,
    item: WishlistItem,
    isRemoving: Boolean,
    onRemoveFromWishlist: () -> Unit,
    onAddToCart: () -> Unit,
    onItemClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorScheme.onSecondary)
            .border(
                width = 1.dp,
                color = colorScheme.secondary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onItemClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(
            modifier = Modifier
                .width(110.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = colorScheme.secondary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.imageCover,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1F)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.title ?: "",
                    modifier = Modifier.weight(1F),
                    color = colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600
                )
                Box(
                    modifier = Modifier
                        .clickable{
                            onRemoveFromWishlist()
                        }
                        .size(30.dp)
                        .dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(
                                radius = 10.dp,
                                spread = 0.dp,
                                color = Color.Black.copy(alpha = 0.15f),
                                offset = DpOffset(0.dp, 5.dp)
                            )
                        )
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_wishlist_filled),
                        contentDescription = null,
                        tint = colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (item.brand != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.brand ?: "",
                    color = colorScheme.onBackground.copy(alpha = 0.6F),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W400
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EGP ${item.price ?: 0}",
                    color = colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.weight(1F))
                if (isRemoving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colorScheme.secondary
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorScheme.secondary)
                            .clickable { onAddToCart() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_to_cart),
                            color = colorScheme.onSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistEmptyState(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.your_wishlist_is_empty),
            color = colorScheme.tertiary,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400
        )
    }
}
