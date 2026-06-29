package com.route.e_commercec43gsunwed.screens.productsDetails

import com.route.domain.model.Result
import com.route.domain.model.products.ProductDetailsData
import com.route.e_commercec43gsunwed.screens.cart.CartContract
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ProductDetailsContract {
    interface ViewModel {
        fun handleActions(action: Actions)
        val states: StateFlow<States>
        val events: SharedFlow<Events>
    }

    sealed interface Actions {
        data object Idle : Actions
        data class GetProductDetails(val productId: String?) : Actions
        data object ClickedOnSearch : Actions
        data object ClickedOnCart : Actions
        data object ClickedOnBack : Actions
        data object ClickedAddToCart : Actions
        object Increment : Actions

        object Decrement : Actions

    }

    sealed interface Events {
        data object Idle : Events
        data object NavigateToCart : Events

        data class ShowMessage(
            val message: String
        ) : Events
        data object NavigateBack : Events
    }

    data class States(val productDetails: Result<ProductDetailsData?>? = null, val cartItemsCount: Int = 0,  val quantity: Int = 1)
}
