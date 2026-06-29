package com.route.e_commercec43gsunwed.destinations

import com.route.domain.model.products.ProductItem
import kotlinx.serialization.Serializable


sealed interface AppRoutes {
    @Serializable
    object SplashDestination

    @Serializable
    object LoginDestination

    @Serializable
    object RegistrationDestination

    @Serializable
    data class ProductsDestination(val subCategoryID: String?)

    @Serializable
    data class ProductDetailsDestination(val productId: String?)

    @Serializable
    object HomeDestination
    @Serializable
    object CartDestination
}
