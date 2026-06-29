package com.route.e_commercec43gsunwed.screens.products

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.model.Result
import com.route.domain.model.products.ProductItem
import com.route.e_commercec43gsunwed.LocalNavController
import com.route.e_commercec43gsunwed.destinations.AppRoutes
import com.route.e_commercec43gsunwed.utils.ECommerceSearchAppBar
import com.route.e_commercec43gsunwed.utils.ProductCard

@Composable
fun ProductsScreen(modifier: Modifier = Modifier, subCategoryId: String?) {
    val viewModel: ProductsViewModel = hiltViewModel()
    val state = viewModel.states.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    val productsState = state.value.products
    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        Log.e("TAG Sub", "ProductsScreen: Sub Category Id = $subCategoryId ")
        viewModel.handleActions(ProductsContract.Actions.GetProducts(subCategoryId))
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is ProductsContract.Events.AddToCartEvent -> {}
                is ProductsContract.Events.AddToWishlistEvent -> {}
                ProductsContract.Events.Idle -> {}
                ProductsContract.Events.NavigateToCart -> {
                    navController.navigate(AppRoutes.CartDestination)
                }
                is ProductsContract.Events.NavigateToProductDetails -> {
                    navController.navigate(AppRoutes.ProductDetailsDestination(it.product?.id))
                }
            }
        }
    }
    Scaffold(modifier = modifier, containerColor = colorScheme.onSecondary) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ECommerceSearchAppBar(
                modifier = Modifier,
                onCartClick = {
                    viewModel.handleActions(
                        ProductsContract.Actions.ClickedOnCart
                    )
                },
                onSearchClick = {},
                cartItemsCount = state.value.cartItemsCount)
            when (productsState) {
                is Result.Error -> {

                }

                is Result.Success -> {
                    ProductsLazyGrid(
                        products = productsState.data ?: listOf(),
                        viewModel = viewModel
                    )
                }

                null -> {}
            }

        }


    }
}

@Composable
fun ProductsLazyGrid(
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel,
    products: List<ProductItem>
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) {
            ProductCard(modifier = Modifier, product = it, onProductClick = { product ->
                viewModel.handleActions(ProductsContract.Actions.ClickedOnProduct(product))
            }, onAddCartClick = { product ->
                viewModel.handleActions(
                    ProductsContract.Actions.ClickedAddToCart(product)
                )
            }, onAddWishlistClick = {

            })
        }
    }
}
