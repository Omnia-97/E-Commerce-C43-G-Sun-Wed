package com.route.e_commercec43gsunwed.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.model.Result
import com.route.domain.usecases.cart.ClearCartUseCase
import com.route.domain.usecases.cart.GetCartUseCase
import com.route.domain.usecases.cart.RemoveCartItemUseCase
import com.route.domain.usecases.cart.UpdateCartItemQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val cartManager: CartManager
) : ViewModel(), CartContract.ViewModel {

    private val _states = MutableStateFlow(CartContract.States())
    override val states: StateFlow<CartContract.States>
        get() = _states

    private val _events = MutableSharedFlow<CartContract.Events>()
    override val events: SharedFlow<CartContract.Events>
        get() = _events

    override fun handleActions(action: CartContract.Actions) {
        viewModelScope.launch {
            when (action) {
                CartContract.Actions.Idle -> {}

                CartContract.Actions.GetCart -> getCart()

                is CartContract.Actions.IncrementQuantity -> {
                    val productId = action.item.productId
                    val newCount = (action.item.count ?: 0) + 1
                    if (productId != null) {
                        updateQuantity(productId, newCount)
                    }
                }

                is CartContract.Actions.DecrementQuantity -> {
                    val productId = action.item.productId
                    val currentCount = action.item.count ?: 0
                    if (productId != null && currentCount > 1) {
                        updateQuantity(productId, currentCount - 1)
                    }
                }

                is CartContract.Actions.RemoveItem -> {
                    action.item.productId?.let { removeItem(it) }
                }

                CartContract.Actions.ClickedOnSearch -> _events.emit(CartContract.Events.NavigateToSearch)
                CartContract.Actions.ClickedOnBack -> _events.emit(CartContract.Events.NavigateBack)
                CartContract.Actions.ClickedCheckout -> _events.emit(CartContract.Events.NavigateToCheckout)
                CartContract.Actions.ClearCart -> clearCart()
            }
        }
    }

    private fun getCart() {
        viewModelScope.launch {
            _states.value = _states.value.copy(isLoading = true)
            getCartUseCase.invoke().collect {
                _states.value = _states.value.copy(cart = it, isLoading = false)
                if (it is Result.Success) {
                    cartManager.updateCount(it.data?.numOfCartItems ?: 0)
                }
            }
        }
    }

    private fun updateQuantity(productId: String, count: Int) {
        viewModelScope.launch {
            _states.value = _states.value.copy(isUpdatingItemId = productId)
            updateCartItemQuantityUseCase.invoke(productId, count).collect {
                when (it) {
                    is Result.Error -> {
                        _states.value = _states.value.copy(isUpdatingItemId = null)
                        _events.emit(CartContract.Events.ShowMessage(it.failure.message))
                    }

                    is Result.Success -> {
                        _states.value = _states.value.copy(cart = it, isUpdatingItemId = null)
                        cartManager.updateCount(it.data?.numOfCartItems ?: 0)
                    }
                }
            }
        }
    }

    private fun removeItem(productId: String) {
        viewModelScope.launch {
            _states.value = _states.value.copy(isUpdatingItemId = productId)
            removeCartItemUseCase.invoke(productId).collect {
                when (it) {
                    is Result.Error -> {
                        _states.value = _states.value.copy(isUpdatingItemId = null)
                        _events.emit(CartContract.Events.ShowMessage(it.failure.message))
                    }

                    is Result.Success -> {
                        _states.value = _states.value.copy(cart = it, isUpdatingItemId = null)
                        cartManager.updateCount(it.data?.numOfCartItems ?: 0)
                    }
                }
            }
        }
    }
    private fun clearCart() {
        viewModelScope.launch {
            _states.value = _states.value.copy(isLoading = true)
            clearCartUseCase.invoke().collect {
                when (it) {
                    is Result.Success -> {
                        _states.value = _states.value.copy(
                            cart = Result.Success(null),
                            isLoading = false
                        )
                        cartManager.updateCount(0)
                    }
                    is Result.Error -> {
                        _states.value = _states.value.copy(isLoading = false)
                        _events.emit(CartContract.Events.ShowMessage(it.failure.message))
                    }
                }
            }
        }
    }
}