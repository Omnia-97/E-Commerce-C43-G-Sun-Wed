package com.route.e_commercec43gsunwed.screens.home.composable.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.model.Result
import com.route.domain.model.categories.CategoryItem
import com.route.domain.model.categories.SubCategoryItem
import com.route.e_commercec43gsunwed.LocalNavController
import com.route.e_commercec43gsunwed.destinations.AppRoutes
import com.route.e_commercec43gsunwed.utils.ECommerceSearchAppBar

@Composable
fun CategoriesTab(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<CategoriesViewModel>()
    val state = viewModel.states.collectAsStateWithLifecycle()
    val categoriesState = state.value.categoriesList
    val subCategoriesState = state.value.subCategoriesList
    val colorScheme = MaterialTheme.colorScheme
    val navController = LocalNavController.current
    var selectedCategory by remember { mutableStateOf<CategoryItem?>(null) }
    LaunchedEffect(Unit) {
        viewModel.handleActions(CategoriesContract.Action.GetCategories)
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                CategoriesContract.Events.Idle -> {}
                CategoriesContract.Events.NavigateToCart -> {
                    navController.navigate(AppRoutes.CartDestination)
                }
                is CategoriesContract.Events.NavigateToProducts -> {
                    navController.navigate(
                        AppRoutes.ProductsDestination(
//                        it.subCategoryId
                            selectedCategory?.id
                        )
                    )
                }

                CategoriesContract.Events.NavigateToSearch -> {}
            }
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        ECommerceSearchAppBar(modifier = Modifier, onCartClick = {
            viewModel.handleActions(CategoriesContract.Action.ClickedOnCart)
        }, onSearchClick = {
            viewModel.handleActions(CategoriesContract.Action.ClickedOnSearch)
        },
            cartItemsCount = state.value.cartItemsCount
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            // 1- Categories
            when (categoriesState) {
                is Result.Error -> {

                }

                is Result.Success -> {
                    CategoriesTabsLazyColumn(
                        modifier = Modifier.fillMaxWidth(0.34F),
                        categoriesList = categoriesState.data ?: listOf(),
                        selectedCategory = selectedCategory,
                        onCategorySelected = {
                            viewModel.handleActions(CategoriesContract.Action.SelectCategory(it))
                            selectedCategory = it
                        }
                    )
                }

                null -> {
                    CircularProgressIndicator(color = colorScheme.secondary)
                }
            }
            when (subCategoriesState) {
                is Result.Error -> {}
                is Result.Success -> {
                    SubCategoryLazyVerticalGrid(
                        modifier = Modifier,
                        subCategories = subCategoriesState.data ?: listOf()
                    ) {
                        viewModel.handleActions(
                            CategoriesContract.Action.SelectSubCategory(
                                it
                            )
                        )
                    }
                }

                null -> {

                }
            }
            // 2- Sub-Categories
        }
    }
}

@Composable
fun CategoriesTabsLazyColumn(
    modifier: Modifier = Modifier,
    categoriesList: List<CategoryItem>,
    selectedCategory: CategoryItem?,
    onCategorySelected: (CategoryItem) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    LazyColumn(
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp)
            .background(
                color = colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        items(categoriesList) {
            CategoryTab(
                modifier = Modifier,
                category = it,
                onCategorySelected = onCategorySelected,
                isSelected = selectedCategory == it
            )
        }

    }
}

@Composable
fun SubCategoryLazyVerticalGrid(
    modifier: Modifier = Modifier,
    subCategories: List<SubCategoryItem>,
    onSubCategorySelected: (SubCategoryItem) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(subCategories.size) {
            SubCategoryCard(
                modifier = Modifier,
                subCategory = subCategories.get(it),
                onSubCategorySelected = onSubCategorySelected
            )
        }

    }
}

@Composable
fun SubCategoryCard(
    modifier: Modifier = Modifier,
    subCategory: SubCategoryItem,
    onSubCategorySelected: (SubCategoryItem) -> Unit
) {
    Text(
        text = subCategory.name ?: "", modifier = modifier.clickable(true) {
            onSubCategorySelected(subCategory)
        }, fontSize = 14.sp,
        fontWeight = FontWeight.W400, textAlign = TextAlign.Center
    )
}

@Composable
fun CategoryTab(
    modifier: Modifier = Modifier,
    category: CategoryItem,
    isSelected: Boolean = false,
    onCategorySelected: (CategoryItem) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(color = if (isSelected) colorScheme.onSecondary else colorScheme.secondaryContainer)
            .clickable(true) {
                if (!isSelected)
                    onCategorySelected(category)
            },
        verticalAlignment = Alignment.CenterVertically,

        ) {
        if (isSelected)
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .height(70.dp)
                    .width(6.dp)
                    .background(colorScheme.secondary)
            )
        Text(category.name ?: "", modifier = Modifier.background(colorScheme.secondaryContainer))
    }
}

@Preview
@Composable
private fun CategoryTabPreview1() {
    CategoryTab(category = CategoryItem(name = "Men's Fashion"), isSelected = true) { }
}

@Preview
@Composable
private fun CategoryTabPreview2() {
    CategoryTab(category = CategoryItem(name = "Men's Fashion"), isSelected = false) { }
}
