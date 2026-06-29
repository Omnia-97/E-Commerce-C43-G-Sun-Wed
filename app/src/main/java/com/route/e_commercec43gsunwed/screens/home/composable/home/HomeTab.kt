package com.route.e_commercec43gsunwed.screens.home.composable.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.model.Result
import com.route.domain.model.categories.CategoryItem
import com.route.domain.model.products.ProductItem
import com.route.e_commercec43gsunwed.LocalNavController
import com.route.e_commercec43gsunwed.R
import com.route.e_commercec43gsunwed.destinations.AppRoutes
import com.route.e_commercec43gsunwed.screens.home.HomeViewModel
import com.route.e_commercec43gsunwed.utils.CategoryCard
import com.route.e_commercec43gsunwed.utils.ECommerceSearchAppBar
import com.route.e_commercec43gsunwed.utils.ProductCard
import com.route.e_commercec43gsunwed.utils.pager.ECommerceHorizontalPager

@Composable
fun HomeTab(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.states.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getCategories()
        viewModel.getProducts()
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                HomeContract.Events.Idle -> {}
                HomeContract.Events.NavigateToCart -> {
                    navController.navigate(AppRoutes.CartDestination)
                }
                is HomeContract.Events.NavigateToProductDetails -> {
                    navController.navigate(AppRoutes.ProductDetailsDestination(it.product?.id))
                }

                HomeContract.Events.NavigateToSearch -> {}
                is HomeContract.Events.NavigateToSubCategory -> {}
                is HomeContract.Events.ShowMessage -> {}
            }
        }
    }
    LazyColumn(
        modifier = modifier
    ) {
        item {
            ECommerceSearchAppBar(onCartClick = {
                viewModel.handleAction(HomeContract.Actions.ClickOnCart)
            }, onSearchClick = {
                viewModel.handleAction(HomeContract.Actions.ClickedOnSearch)
            },
                cartItemsCount = state.value.cartItemsCount

            )
        }
        item {
            ECommerceHorizontalPager(modifier = Modifier.padding(top = 8.dp))
        } // E-Commerce Categories Home
        item {
            Row(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.categories),
                    color = colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Spacer(Modifier.weight(1F))
                Text(
                    stringResource(R.string.view_all),
                    color = colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400
                )
            }
        }
        val categoriesState = state.value.categories

        when (categoriesState) {
            is Result.Error -> {}
            is Result.Success -> {
                item {
                    CategoriesGrid(
                        modifier = Modifier,
                        categories = categoriesState.data
                    )
                }
            }

            null -> {

            }
        }
        val productsState = state.value.products
        when (productsState) {
            is Result.Error -> {
                Log.e("TAG", "HomeTab: Error ${productsState.failure.message}")

            }

            is Result.Success -> {
                Log.e("TAG", "HomeTab: Success ${productsState.data}")
                item {
                    ProductsLazyRow(modifier = Modifier, productsState.data, viewModel = viewModel)
                }
            }

            null -> {}
        }
    }
}

@Composable
fun ProductsLazyRow(
    modifier: Modifier = Modifier,
    products: List<ProductItem>?,
    viewModel: HomeViewModel
) {
    if (products != null)
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(products) {
                ProductCard(
                    modifier = Modifier,
                    product = it,
                    onProductClick = {
                        viewModel.handleAction(HomeContract.Actions.ClickedOnProduct(it))
                    },
                    onAddCartClick = {
                        viewModel.handleAction(HomeContract.Actions.ClickOnCart)
                    },
                    onAddWishlistClick = {

                    })
            }

        }
}

@Composable
fun CategoriesGrid(modifier: Modifier = Modifier, categories: List<CategoryItem>?) {
    if (categories != null)
        LazyHorizontalGrid(modifier = modifier.height(250.dp), rows = GridCells.Fixed(2)) {
            items(categories) {
                CategoryCard(modifier = Modifier, category = it)
            }
        }
}