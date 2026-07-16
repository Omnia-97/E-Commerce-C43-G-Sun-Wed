package com.route.e_commercec43gsunwed.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.model.Result
import com.route.domain.model.products.ProductItem
import com.route.domain.usecases.cart.AddToCartUseCase
import com.route.domain.usecases.products.GetProductsUseCase
import com.route.domain.usecases.wishlist.AddToWishlistUseCase
import com.route.domain.usecases.wishlist.GetWishlistUseCase
import com.route.domain.usecases.wishlist.RemoveFromWishlistUseCase
import com.route.domain.wishlist.WishlistManager
import com.route.e_commercec43gsunwed.screens.products.ProductsContract.Events.NavigateToProductDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val cartManager: CartManager,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val wishlistManager: WishlistManager,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase
) : ViewModel(), ProductsContract.ViewModel {
    override fun handleActions(actions: ProductsContract.Actions) {
        viewModelScope.launch {
            when (actions) {
                is ProductsContract.Actions.ClickedAddToCart -> {
                    addToCart(actions.product)
                }

                is ProductsContract.Actions.ClickedAddToWishlist -> {}
                ProductsContract.Actions.ClickedOnCart -> {
                    _events.emit(
                        ProductsContract.Events.NavigateToCart
                    )
                }

                is ProductsContract.Actions.ClickedOnProduct -> {
                    _events.emit(NavigateToProductDetails(actions.product))
                }

                ProductsContract.Actions.Idle -> {}
                is ProductsContract.Actions.GetProducts -> {
                    getProducts(actions.subCategoryId)
                }

                is ProductsContract.Actions.ToggleWishlist -> {
                    toggleWishlist(actions.product?.id)
                }
            }
        }
    }

    private val _events = MutableSharedFlow<ProductsContract.Events>()
    override val events: SharedFlow<ProductsContract.Events>
        get() = _events.asSharedFlow()
    private val _states = MutableStateFlow(ProductsContract.States(null))
    override val states: StateFlow<ProductsContract.States>
        get() = _states

    init {
        viewModelScope.launch {
            cartManager.count.collect { count ->
                _states.value =
                    _states.value.copy(
                        cartItemsCount = count
                    )
            }
        }
    }

    val wishlistIds: StateFlow<Set<String>> = wishlistManager.wishlistIds

    fun getProducts(subCategoryId: String?) {
        viewModelScope.launch {
            getProductsUseCase.invoke(subCategoryId).collect {
                _states.value = _states.value.copy(products = it)
            }
        }
    }

    private fun addToCart(product: ProductItem?) {
        val productId = product?.id ?: return

        viewModelScope.launch {
            addToCartUseCase.invoke(productId).collect { result ->

                when (result) {

                    is Result.Success -> {

                        cartManager.updateCount(
                            result.data?.numOfCartItems ?: 0
                        )

                        _events.emit(
                            ProductsContract.Events.AddToCartEvent(product)
                        )
                    }

                    is Result.Error -> {

                    }
                }
            }
        }
    }

    private fun toggleWishlist(productId: String?) {
        productId ?: return
        viewModelScope.launch {
            val isCurrentlyInWishlist = wishlistManager.isInWishlist(productId)
            if (isCurrentlyInWishlist) {
                removeFromWishlistUseCase.invoke(productId).collect { result ->
                    if (result is Result.Success) {
                        wishlistManager.update(result.data ?: emptySet())
                    }
                }
            } else {
                addToWishlistUseCase.invoke(productId).collect { result ->
                    if (result is Result.Success) {
                        wishlistManager.update(result.data ?: emptySet())
                    }
                }
            }
        }
    }
}
