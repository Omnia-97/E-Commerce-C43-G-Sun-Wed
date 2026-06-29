package com.route.e_commercec43gsunwed.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.model.Result
import com.route.domain.model.products.ProductItem
import com.route.domain.usecases.cart.AddToCartUseCase
import com.route.domain.usecases.products.GetProductsUseCase
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
    private val cartManager: CartManager
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
                    _events.emit(ProductsContract.Events.NavigateToProductDetails(actions.product))
                }

                ProductsContract.Actions.Idle -> {}
                is ProductsContract.Actions.GetProducts -> {
                    getProducts(actions.subCategoryId)
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
}
