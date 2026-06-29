package com.route.e_commercec43gsunwed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.route.e_commercec43gsunwed.destinations.AppRoutes
import com.route.e_commercec43gsunwed.screens.auth.login.LoginScreen
import com.route.e_commercec43gsunwed.screens.auth.registration.RegistrationScreen
import com.route.e_commercec43gsunwed.screens.cart.CartScreen
import com.route.e_commercec43gsunwed.screens.home.HomeScreen
import com.route.e_commercec43gsunwed.screens.products.ProductsScreen
import com.route.e_commercec43gsunwed.screens.productsDetails.ProductDetailsScreen
import com.route.e_commercec43gsunwed.screens.splash.SplashScreen
import com.route.e_commercec43gsunwed.ui.theme.ECommerceC43GSunWedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceC43GSunWedTheme {
                ECommerce()
            }
        }
    }
}

// Module-based Clean Architecture
val LocalNavController = compositionLocalOf<NavHostController> {
    error("No Local Nav Controller ")
}

@Composable
fun ECommerce(modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navHostController) {
        Scaffold(modifier) { paddingValues ->
            paddingValues

            NavHost(
                navController = navHostController,
                modifier = Modifier,
                startDestination = AppRoutes.SplashDestination
            ) {
                composable<AppRoutes.SplashDestination> {
                    SplashScreen()
                }
                composable<AppRoutes.LoginDestination> {
                    LoginScreen()
                }
                composable<AppRoutes.RegistrationDestination> {
                    RegistrationScreen()
                }
                composable<AppRoutes.HomeDestination> {
                    HomeScreen()
                }
                composable<AppRoutes.ProductsDestination> {
                    val productArgs = it.toRoute<AppRoutes.ProductsDestination>()
                    ProductsScreen(subCategoryId = productArgs.subCategoryID)
                }
                composable<AppRoutes.ProductDetailsDestination> {
                    val productArgs = it.toRoute<AppRoutes.ProductDetailsDestination>()
                    ProductDetailsScreen(productItemId = productArgs.productId)
                }
                composable<AppRoutes.CartDestination> {
                    CartScreen()
                }
            }
        }
    }
}

