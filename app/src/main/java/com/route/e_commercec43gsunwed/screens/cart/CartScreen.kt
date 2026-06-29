package com.route.e_commercec43gsunwed.screens.cart

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.route.domain.model.Result
import com.route.domain.model.cart.CartProductItem
import com.route.e_commercec43gsunwed.LocalNavController
import com.route.e_commercec43gsunwed.R
import com.route.e_commercec43gsunwed.utils.ErrorDialog
import com.route.e_commercec43gsunwed.utils.ProductDetailsToolbar

@Composable
fun CartScreen(modifier: Modifier = Modifier) {
    val viewModel: CartViewModel = hiltViewModel()
    val state = viewModel.states.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    val navController = LocalNavController.current
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.handleActions(CartContract.Actions.GetCart)
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                CartContract.Events.Idle -> {}
                CartContract.Events.NavigateBack -> navController.popBackStack()
                CartContract.Events.NavigateToSearch -> {}
                CartContract.Events.NavigateToCheckout -> {}
                is CartContract.Events.ShowMessage -> errorMessage = it.message
            }
        }
    }

    Scaffold(modifier = modifier, containerColor = colorScheme.onSecondary) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ProductDetailsToolbar(
                onSearchClick = { viewModel.handleActions(CartContract.Actions.ClickedOnSearch) },
                onBackClick = { viewModel.handleActions(CartContract.Actions.ClickedOnBack) },
                onCartClick = { },
                title = stringResource(R.string.cart)
            )

            when (val cartState = state.value.cart) {
                null -> {
                    if (state.value.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colorScheme.secondary)
                        }
                    }
                }

                is Result.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = cartState.failure.message
                                ?: stringResource(R.string.something_went_wrong),
                            color = colorScheme.tertiary
                        )
                    }
                }

                is Result.Success -> {
                    val products = cartState.data?.products ?: listOf()
                    if (products.isEmpty()) {
                        CartEmptyState(modifier = Modifier.weight(1F))
                    } else {
                        CartItemsLazyColumn(
                            modifier = Modifier.weight(1F),
                            products = products,
                            updatingItemId = state.value.isUpdatingItemId,
                            onIncrement = {
                                viewModel.handleActions(CartContract.Actions.IncrementQuantity(it))
                            },
                            onDecrement = {
                                viewModel.handleActions(CartContract.Actions.DecrementQuantity(it))
                            },
                            onRemove = {
                                viewModel.handleActions(CartContract.Actions.RemoveItem(it))
                            }
                        )
                        CartCheckoutFooter(
                            totalPrice = cartState.data?.totalCartPrice,
                            onCheckoutClick = {
                                viewModel.handleActions(CartContract.Actions.ClickedCheckout)
                            }
                        )
                    }
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
fun CartItemsLazyColumn(
    modifier: Modifier = Modifier,
    products: List<CartProductItem>,
    updatingItemId: String?,
    onIncrement: (CartProductItem) -> Unit,
    onDecrement: (CartProductItem) -> Unit,
    onRemove: (CartProductItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            products,
            key = { it.cartItemId ?: it.productId ?: it.hashCode().toString() }) { item ->
            CartItemRow(
                item = item,
                isUpdating = updatingItemId == item.productId,
                onIncrement = { onIncrement(item) },
                onDecrement = { onDecrement(item) },
                onRemove = { onRemove(item) }
            )
        }
    }
}

@Composable
fun CartItemRow(
    modifier: Modifier = Modifier,
    item: CartProductItem,
    isUpdating: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.onSecondary),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = item.imageCover,
            contentDescription = item.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1F)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = item.title ?: "",
                    modifier = Modifier.weight(1F),
                    color = colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500
                )
                Image(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.remove_from_cart),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(enabled = !isUpdating) { onRemove() }
                )
            }
            if (item.brandName != null) {
                Text(
                    text = item.brandName ?: "",
                    color = colorScheme.tertiary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
            }
            Spacer(Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.price ?: 0}",
                    color = colorScheme.secondary,
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp
                )
                Text(
                    text = " ${stringResource(R.string.egp)}",
                    color = colorScheme.secondary,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
                Spacer(Modifier.weight(1F))
                if (isUpdating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colorScheme.secondary
                    )
                } else {
                    CartQuantityStepper(
                        count = item.count ?: 1,
                        onIncrement = onIncrement,
                        onDecrement = onDecrement
                    )
                }
            }
        }
    }
}

@Composable
fun CartQuantityStepper(
    modifier: Modifier = Modifier,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .background(colorScheme.secondary, RoundedCornerShape(20.dp))
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_minus),
            contentDescription = stringResource(R.string.icon_decrement_product_from_cart),
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable(enabled = count > 1) { onDecrement() }
        )
        Text(
            text = "$count",
            color = colorScheme.onSecondary,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = stringResource(R.string.icon_increment_product_into_cart),
            modifier = Modifier
                .padding(end = 12.dp)
                .clickable { onIncrement() }
        )
    }
}

@Composable
fun CartCheckoutFooter(
    modifier: Modifier = Modifier,
    totalPrice: Double?,
    onCheckoutClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(R.string.total_price),
                color = colorScheme.tertiary,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400
            )
            Text(
                text = "${totalPrice ?: 0} ${stringResource(R.string.egp)}",
                color = colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
        }
        Spacer(Modifier.weight(1F))
        Row(
            modifier = Modifier
                .background(colorScheme.onBackground, RoundedCornerShape(8.dp))
                .clickable { onCheckoutClick() }
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.check_out),
                color = colorScheme.onSecondary,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )
            Spacer(Modifier.size(8.dp))
            Text(text = "→", color = colorScheme.onSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
fun CartEmptyState(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.your_cart_is_empty),
            color = colorScheme.tertiary,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CartScreenPreview() {
    CartScreen()
}