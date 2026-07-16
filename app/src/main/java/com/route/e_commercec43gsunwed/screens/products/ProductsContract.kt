package com.route.e_commercec43gsunwed.screens.products

import com.route.domain.model.Result
import com.route.domain.model.products.ProductItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ProductsContract {
    interface ViewModel {
        fun handleActions(actions: Actions)
        val events: SharedFlow<Events>
        val states: StateFlow<States>
    }

    data class States(
        val products: Result<List<ProductItem>>? = null, val cartItemsCount: Int = 0
    )

    sealed interface Actions {
        data object Idle : Actions
        data object ClickedOnCart : Actions
        data class GetProducts(val subCategoryId: String? = null) : Actions
        data class ClickedOnProduct(val product: ProductItem?) : Actions
        data class ClickedAddToWishlist(val product: ProductItem?) : Actions
        data class ClickedAddToCart(val product: ProductItem?) : Actions
        data class ToggleWishlist(val product: ProductItem?) : Actions
    }

    sealed interface Events {
        data object Idle : Events
        data object NavigateToCart : Events
        data class NavigateToProductDetails(val product: ProductItem?) : Events
        data class AddToWishlistEvent(val product: ProductItem?) : Events
        data class AddToCartEvent(val product: ProductItem?) : Events
    }

}
