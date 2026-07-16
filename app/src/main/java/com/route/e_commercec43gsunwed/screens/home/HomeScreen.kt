package com.route.e_commercec43gsunwed.screens.home



import androidx.compose.foundation.layout.padding


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import com.route.e_commercec43gsunwed.screens.home.composable.account.AccountTab
import com.route.e_commercec43gsunwed.screens.home.composable.categories.CategoriesTab
import com.route.e_commercec43gsunwed.screens.home.composable.home.HomeTab
import com.route.e_commercec43gsunwed.screens.home.composable.wishlist.WishlistTab
import com.route.e_commercec43gsunwed.utils.ECommerceBottomNav

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedBottomNavIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier,
        containerColor = colorScheme.onSecondary,
        bottomBar = {
            ECommerceBottomNav {
                selectedBottomNavIndex = it
            }
        }
    ) { paddingValues ->
        paddingValues
        when (selectedBottomNavIndex) {
            0 -> HomeTab(modifier = Modifier.padding(paddingValues))
            1 -> CategoriesTab(modifier = Modifier.padding(paddingValues))
            2 -> WishlistTab(modifier = Modifier.padding(paddingValues))
            3 -> AccountTab()
        }
    }
}



