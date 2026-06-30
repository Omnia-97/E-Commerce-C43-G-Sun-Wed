package com.route.e_commercec43gsunwed.screens.productsDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.model.Result
import com.route.domain.model.products.ProductDetailsData
import com.route.e_commercec43gsunwed.LocalNavController
import com.route.e_commercec43gsunwed.R
import com.route.e_commercec43gsunwed.destinations.AppRoutes
import com.route.e_commercec43gsunwed.utils.ProductDetailsToolbar
import com.route.e_commercec43gsunwed.utils.pager.ProductDetailImagesPager

@Composable
fun ProductDetailsScreen(modifier: Modifier = Modifier, productItemId: String?) {
    Log.e("TAG", "ProductDetailsScreen: $productItemId")
    val viewModel: ProductDetailsViewModel = hiltViewModel()
    val states = viewModel.states.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.handleActions(
            ProductDetailsContract.Actions.GetProductDetails(productItemId),
        )
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                ProductDetailsContract.Events.NavigateToCart -> {
                    navController.navigate(AppRoutes.CartDestination)
                }

                is ProductDetailsContract.Events.ShowMessage -> {
                }

                ProductDetailsContract.Events.NavigateBack -> {
                    navController.popBackStack()
                }

                else -> {}
            }
        }
    }
    Scaffold(modifier = modifier, containerColor = colorScheme.onSecondary)
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProductDetailsToolbar(
                onSearchClick = {
                    viewModel.handleActions(ProductDetailsContract.Actions.ClickedOnSearch)
                },

                onBackClick = {
                    viewModel.handleActions(ProductDetailsContract.Actions.ClickedOnBack)

                },

                onCartClick = {
                    viewModel.handleActions(ProductDetailsContract.Actions.ClickedOnCart)
                },

                cartItemsCount = states.value.cartItemsCount
            )

            val productDetailsState = states.value.productDetails
            when (productDetailsState) {
                is Result.Error -> {
                    Log.e(
                        "TAG Error",
                        "ProductDetailsScreen: ${productDetailsState.failure.message}",
                    )
                }

                is Result.Success -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        ProductDetailImagesPager(modifier = Modifier, productDetailsState.data)
                        ProductNamePriceRow(modifier = Modifier, productDetailsState.data)
                        ProductRatingCartRow(
                            modifier = Modifier,
                            productDetailsState.data,
                            quantity = states.value.quantity,
                            onIncrement = {
                                viewModel.handleActions(ProductDetailsContract.Actions.Increment)
                            },
                            onDecrement = {
                                viewModel.handleActions(ProductDetailsContract.Actions.Decrement)
                            }
                        )
                        ProductDescriptionColumn(modifier = Modifier, productDetailsState.data)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ProductDetailTotalPrice(
                            productDetails = productDetailsState.data,
                            quantity = if (states.value.quantity == 0) 1 else states.value.quantity,
                            isAlreadyInCart = states.value.quantity > 0,
                            onAddToCart = {
                                viewModel.handleActions(ProductDetailsContract.Actions.ClickedAddToCart)
                            }
                        )
                    }
                }

                null -> {}
            }
        }

    }
}

@Composable
fun ProductDescriptionColumn(modifier: Modifier = Modifier, productDetails: ProductDetailsData?) {
    val colorScheme = MaterialTheme.colorScheme
    if (productDetails?.description != null)
        Column(modifier = modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.description),
                color = colorScheme.onPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = productDetails.description ?: "",
                color = colorScheme.onPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
}

@Composable
fun ProductRatingCartRow(
    modifier: Modifier = Modifier,
    productDetails: ProductDetailsData?,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    quantity: Int
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (productDetails?.sold != null)
            Text(
                "${productDetails.sold} Sold", modifier = Modifier
                    .border(
                        1.dp, colorScheme.secondary.copy(alpha = 0.3F),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(
                        vertical = 8.dp, horizontal = 16.dp
                    ),
                color = colorScheme.onPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500
            )
        Spacer(Modifier.size(16.dp))
        Image(painter = painterResource(R.drawable.ic_rating), contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            "${productDetails?.ratingsAverage}", color = colorScheme.onPrimary,
            fontSize = 14.sp, fontWeight = FontWeight.W400
        )
        Text(
            " ( ${productDetails?.ratingsQuantity} )", color = colorScheme.onPrimary,
            fontSize = 14.sp, fontWeight = FontWeight.W400
        )
        Spacer(Modifier.weight(1F))
        ProductCartActions(
            onIncrement = onIncrement,
            onDecrement = onDecrement,
            quantity = quantity
        )
    }
}

@Composable
fun ProductCartActions(
    modifier: Modifier = Modifier, onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    quantity: Int
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .background(colorScheme.secondary, RoundedCornerShape(20.dp))
            .padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_minus),
            contentDescription = stringResource(R.string.icon_decrement_product_from_cart),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable {
                    onDecrement()
                }
        )
        Text(
            "$quantity",
            color = colorScheme.onSecondary,
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 22.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = stringResource(R.string.icon_increment_product_into_cart),
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable {
                    onIncrement()
                }
        )
    }
}

@Composable
fun ProductNamePriceRow(modifier: Modifier = Modifier, productDetails: ProductDetailsData?) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = productDetails?.title ?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            color = colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth(0.4F)
        )
        Spacer(Modifier.weight(1F))
        Text(
            text = "EGP", fontSize = 18.sp,
            fontWeight = FontWeight.W500, color = colorScheme.onPrimary
        )
        Text(
            text = "${productDetails?.price}",
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            color = colorScheme.onPrimary,
        )
    }
}

@Composable
fun ProductDetailTotalPrice(
    modifier: Modifier = Modifier,
    productDetails: ProductDetailsData?,
    quantity: Int,
    isAlreadyInCart: Boolean,
    onAddToCart: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val totalPrice = (productDetails?.price ?: 0) * quantity
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Total price",
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = colorScheme.onBackground.copy(alpha = 0.6F)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "EGP $totalPrice",
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                color = colorScheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        Box(
            modifier = Modifier
                .clickable { onAddToCart() }
                .clip(RoundedCornerShape(20.dp))
                .background(colorScheme.secondary)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cart_plus),
                    contentDescription = null,
                    tint = colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    if (isAlreadyInCart) "In cart" else "Add to cart",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    color = colorScheme.onSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailTotalPricePreview() {
    ProductDetailTotalPrice(
        modifier = Modifier,
        productDetails = ProductDetailsData(
            price = 3500
        ),
        quantity = 1,
        onAddToCart = {},
        isAlreadyInCart = false
    )
}

