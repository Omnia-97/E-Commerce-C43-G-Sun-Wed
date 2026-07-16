package com.route.e_commercec43gsunwed.screens.home.composable.wishlist

import com.route.domain.model.Result
import com.route.domain.model.wishlist.WishlistItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface WishlistContract {
    interface ViewModel {
        fun handleActions(action: Actions)
        val states: StateFlow<States>
        val events: SharedFlow<Events>
    }

    sealed interface Actions {
        data object Idle : Actions
        data object GetWishlist : Actions
        data class RemoveFromWishlist(val productId: String) : Actions
        data class AddToCart(val productId: String) : Actions
        data class ClickedOnProduct(val productId: String) : Actions
    }

    sealed interface Events {
        data object Idle : Events
        data class NavigateToProductDetails(val productId: String) : Events
        data class ShowMessage(val message: String?) : Events
    }

    data class States(
        val wishlist: Result<List<WishlistItem>>? = null,
        val isLoading: Boolean = false,
        val removingProductId: String? = null
    )
}