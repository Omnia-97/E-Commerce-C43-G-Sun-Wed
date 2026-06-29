package com.route.e_commercec43gsunwed.screens.productsDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.model.Result
import com.route.domain.usecases.cart.AddToCartUseCase
import com.route.domain.usecases.products.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val cartManager: CartManager
) : ViewModel(), ProductDetailsContract.ViewModel {
    override fun handleActions(action: ProductDetailsContract.Actions) {
        viewModelScope.launch {
            when (action) {
                ProductDetailsContract.Actions.Idle -> {}
                is ProductDetailsContract.Actions.GetProductDetails -> {
                    getProductDetails(action.productId)
                }

                ProductDetailsContract.Actions.ClickedOnBack -> {
                    _events.emit(ProductDetailsContract.Events.NavigateBack)
                }

                ProductDetailsContract.Actions.ClickedOnCart -> {
                    _events.emit(
                        ProductDetailsContract.Events.NavigateToCart
                    )
                }

                ProductDetailsContract.Actions.ClickedOnSearch -> {}
                ProductDetailsContract.Actions.ClickedAddToCart -> {
                    addToCart()
                }

                ProductDetailsContract.Actions.Decrement -> {
                    decrement()
                }

                ProductDetailsContract.Actions.Increment -> {
                    increment()
                }
            }
        }
    }

    private val _states = MutableStateFlow(ProductDetailsContract.States(null))
    override val states: StateFlow<ProductDetailsContract.States>
        get() = _states
    private val _events = MutableSharedFlow<ProductDetailsContract.Events>()
    override val events: SharedFlow<ProductDetailsContract.Events>
        get() = _events

    init {
        viewModelScope.launch {
            cartManager.count.collect { count ->
                _states.value = _states.value.copy(
                    cartItemsCount = count
                )
            }
        }
    }

    private fun getProductDetails(productId: String?) {
        viewModelScope.launch {
            getProductDetailsUseCase.invoke(productId).collect {
                _states.value = _states.value.copy(productDetails = it)
            }
        }
    }

    private fun addToCart() {
        val productId =
            (_states.value.productDetails as? Result.Success)?.data?.id ?: return

        viewModelScope.launch {
            addToCartUseCase.invoke(productId).collect { result ->

                when (result) {

                    is Result.Success -> {

                        cartManager.updateCount(
                            result.data?.numOfCartItems ?: 0
                        )

                        _events.emit(
                            ProductDetailsContract.Events.ShowMessage(
                                "Added to cart successfully"
                            )
                        )
                    }

                    is Result.Error -> {

                        _events.emit(
                            ProductDetailsContract.Events.ShowMessage(
                                result.failure.message ?: "Something went wrong"
                            )
                        )

                    }

                }

            }
        }
    }

    private fun increment() {
        _states.value =
            _states.value.copy(
                quantity = _states.value.quantity + 1
            )
    }

    private fun decrement() {
        if (_states.value.quantity > 1) {
            _states.value =
                _states.value.copy(
                    quantity = _states.value.quantity - 1
                )
        }
    }

}