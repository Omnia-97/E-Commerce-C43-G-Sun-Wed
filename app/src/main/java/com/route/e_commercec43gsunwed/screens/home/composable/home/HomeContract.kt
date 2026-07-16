package com.route.e_commercec43gsunwed.screens.home.composable.home

import com.route.domain.model.Result
import com.route.domain.model.categories.CategoryItem
import com.route.domain.model.products.ProductItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface HomeContract {
    interface ViewModel {
        fun handleAction(actions: Actions)
        val states: StateFlow<States>
        val events: SharedFlow<Events>
    }

    sealed interface Actions {
        data object Idle : Actions
        data object ClickOnCart : Actions
        data object ClickedOnSearch : Actions
        data class ClickedOnCategory(val category: CategoryItem? = null) : Actions
        data class ClickedOnProduct(val product: ProductItem? = null) : Actions
        data class ToggleWishlist(val product: ProductItem? = null) : Actions
    }

    sealed interface Events {
        data object Idle : Events
        data object NavigateToSearch : Events
        data object NavigateToCart : Events
        data class ShowMessage(val message: String?) : Events
        data class NavigateToProductDetails(val product: ProductItem?) : Events
        data class NavigateToSubCategory(val category: CategoryItem?) : Events
    }

    data class States(
        val categories: Result<List<CategoryItem>>? = null,
        val products: Result<List<ProductItem>>? = null,
        val cartItemsCount: Int = 0
    )

}
