package com.route.e_commercec43gsunwed.screens.home.composable.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.model.Result
import com.route.domain.usecases.cart.AddToCartUseCase
import com.route.domain.usecases.wishlist.GetWishlistUseCase
import com.route.domain.usecases.wishlist.RemoveFromWishlistUseCase
import com.route.domain.wishlist.WishlistManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager,
) : ViewModel(), WishlistContract.ViewModel {

    private val _states = MutableStateFlow(WishlistContract.States())
    override val states: StateFlow<WishlistContract.States>
        get() = _states

    private val _events = MutableSharedFlow<WishlistContract.Events>()
    override val events: SharedFlow<WishlistContract.Events>
        get() = _events

    init {
        loadWishlistIds()
        observeCartCount()
    }
    val wishlistIds: StateFlow<Set<String>> = wishlistManager.wishlistIds


    override fun handleActions(action: WishlistContract.Actions) {
        viewModelScope.launch {
            when (action) {
                WishlistContract.Actions.Idle -> {}

                WishlistContract.Actions.GetWishlist -> getWishlist()

                is WishlistContract.Actions.RemoveFromWishlist ->
                    removeFromWishlist(action.productId)

                is WishlistContract.Actions.AddToCart ->
                    addToCart(action.productId)

                is WishlistContract.Actions.ClickedOnProduct ->
                    _events.emit(
                        WishlistContract.Events.NavigateToProductDetails(action.productId)
                    )
            }
        }
    }

    private fun getWishlist() {
        viewModelScope.launch {
            _states.value = _states.value.copy(isLoading = true)
            getWishlistUseCase.invoke().collect {
                _states.value = _states.value.copy(wishlist = it, isLoading = false)
                if (it is Result.Success) {
                    val ids = it.data?.mapNotNull { item -> item.id }?.toSet() ?: emptySet()
                    wishlistManager.update(ids)
                }
            }
        }
    }

    private fun removeFromWishlist(productId: String) {
        viewModelScope.launch {
            _states.value = _states.value.copy(removingProductId = productId)
            removeFromWishlistUseCase.invoke(productId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        wishlistManager.update(result.data ?: emptySet())
                        getWishlist()
                        _states.value = _states.value.copy(removingProductId = null)
                    }

                    is Result.Error -> {
                        _states.value = _states.value.copy(removingProductId = null)
                        _events.emit(WishlistContract.Events.ShowMessage(result.failure.message))
                    }
                }
            }
        }
    }

    private fun addToCart(productId: String) {
        viewModelScope.launch {
            addToCartUseCase.invoke(productId).collect { result ->
                when (result) {
                    is Result.Success ->
                        _events.emit(WishlistContract.Events.ShowMessage("Added to cart successfully"))

                    is Result.Error ->
                        _events.emit(WishlistContract.Events.ShowMessage(result.failure.message))
                }
            }
        }
    }
    private fun loadWishlistIds() {
        viewModelScope.launch {
            getWishlistUseCase.invoke().collect { result ->
                if (result is Result.Success) {
                    val ids = result.data
                        ?.mapNotNull { it.id }
                        ?.toSet()
                        ?: emptySet()
                    wishlistManager.update(ids)
                }
            }
        }
    }
    private fun observeCartCount() {
        viewModelScope.launch {
            cartManager.count.collect {
                _states.value = _states.value.copy(
                    cartItemsCount = it
                )
            }
        }
    }

}