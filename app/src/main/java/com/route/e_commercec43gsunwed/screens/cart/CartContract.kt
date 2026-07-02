package com.route.e_commercec43gsunwed.screens.cart

import com.route.domain.model.Result
import com.route.domain.model.cart.CartData
import com.route.domain.model.cart.CartProductItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CartContract {
    interface ViewModel {
        fun handleActions(action: Actions)
        val states: StateFlow<States>
        val events: SharedFlow<Events>
    }

    sealed interface Actions {
        data object Idle : Actions
        data object GetCart : Actions
        data class IncrementQuantity(val item: CartProductItem) : Actions
        data class DecrementQuantity(val item: CartProductItem) : Actions
        data class RemoveItem(val item: CartProductItem) : Actions
        data object ClickedOnSearch : Actions
        data object ClickedOnBack : Actions
        data object ClickedCheckout : Actions
        data object ClearCart : Actions
    }

    sealed interface Events {
        data object Idle : Events
        data object NavigateBack : Events
        data object NavigateToSearch : Events
        data object NavigateToCheckout : Events
        data class ShowMessage(val message: String?) : Events
    }

    data class States(
        val cart: Result<CartData>? = null,
        val isUpdatingItemId: String? = null,
        val isLoading: Boolean = false
    )
}