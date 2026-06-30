package com.route.e_commercec43gsunwed.screens.productsDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.model.Result
import com.route.domain.usecases.cart.AddToCartUseCase
import com.route.domain.usecases.cart.GetCartUseCase
import com.route.domain.usecases.cart.UpdateCartItemQuantityUseCase
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
    private val getCartUseCase: GetCartUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val cartManager: CartManager
) : ViewModel(), ProductDetailsContract.ViewModel {

    private var currentProductId: String? = null

    private val _states = MutableStateFlow(ProductDetailsContract.States(null))
    override val states: StateFlow<ProductDetailsContract.States>
        get() = _states

    private val _events = MutableSharedFlow<ProductDetailsContract.Events>()
    override val events: SharedFlow<ProductDetailsContract.Events>
        get() = _events

    init {
        viewModelScope.launch {
            cartManager.count.collect { count ->
                _states.value = _states.value.copy(cartItemsCount = count)
            }
        }
    }

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
                    _events.emit(ProductDetailsContract.Events.NavigateToCart)
                }

                ProductDetailsContract.Actions.ClickedOnSearch -> {}

                ProductDetailsContract.Actions.ClickedAddToCart -> addToCart()

                ProductDetailsContract.Actions.Increment -> increment()

                ProductDetailsContract.Actions.Decrement -> decrement()
            }
        }
    }

    private fun getProductDetails(productId: String?) {
        viewModelScope.launch {
            getProductDetailsUseCase.invoke(productId).collect {
                _states.value = _states.value.copy(productDetails = it)
                currentProductId = (it as? Result.Success)?.data?.id
                loadCurrentProductCount()
            }
        }
    }

    private fun loadCurrentProductCount() {
        viewModelScope.launch {
            getCartUseCase.invoke().collect { result ->
                if (result is Result.Success) {
                    val item = result.data?.products?.firstOrNull {
                        it.productId == currentProductId
                    }
                    _states.value = _states.value.copy(quantity = item?.count ?: 0)
                }
            }
        }
    }

    private fun addToCart() {
        val productId = currentProductId ?: return
        viewModelScope.launch {
            _states.value = _states.value.copy(isUpdatingQuantity = true)
            addToCartUseCase.invoke(productId).collect { result ->
                _states.value = _states.value.copy(isUpdatingQuantity = false)
                when (result) {
                    is Result.Success -> {
                        cartManager.updateCount(result.data?.numOfCartItems ?: 0)
                        val newCount = result.data?.products
                            ?.firstOrNull { it.productId == productId }?.count ?: 1
                        _states.value = _states.value.copy(quantity = newCount)
                        _events.emit(ProductDetailsContract.Events.ShowMessage("Added to cart successfully"))
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
        val productId = currentProductId ?: return
        val current = _states.value.quantity
        if (current == 0) {
            addToCart()
            return
        }
        updateQuantity(productId, current + 1)
    }

    private fun decrement() {
        val productId = currentProductId ?: return
        val current = _states.value.quantity
        if (current <= 1) return
        updateQuantity(productId, current - 1)
    }

    private fun updateQuantity(productId: String, newCount: Int) {
        viewModelScope.launch {
            _states.value = _states.value.copy(isUpdatingQuantity = true)
            updateCartItemQuantityUseCase.invoke(productId, newCount).collect { result ->
                _states.value = _states.value.copy(isUpdatingQuantity = false)
                when (result) {
                    is Result.Success -> {
                        cartManager.updateCount(result.data?.numOfCartItems ?: 0)
                        val updatedCount = result.data?.products
                            ?.firstOrNull { it.productId == productId }?.count ?: newCount
                        _states.value = _states.value.copy(quantity = updatedCount)
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
}
//@HiltViewModel
//class ProductDetailsViewModel @Inject constructor(
//    private val getProductDetailsUseCase: GetProductDetailsUseCase,
//    private val addToCartUseCase: AddToCartUseCase,
//    private val getCartUseCase: GetCartUseCase,
//    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
//    private val cartManager: CartManager
//) : ViewModel(), ProductDetailsContract.ViewModel
//{
//    private var currentProductId: String? = null
//    private var cartProductCount = 0
//    override fun handleActions(action: ProductDetailsContract.Actions) {
//        viewModelScope.launch {
//            when (action) {
//                ProductDetailsContract.Actions.Idle -> {}
//                is ProductDetailsContract.Actions.GetProductDetails -> {
//                    getProductDetails(action.productId)
//                }
//
//                ProductDetailsContract.Actions.ClickedOnBack -> {
//                    _events.emit(ProductDetailsContract.Events.NavigateBack)
//                }
//
//                ProductDetailsContract.Actions.ClickedOnCart -> {
//                    _events.emit(
//                        ProductDetailsContract.Events.NavigateToCart
//                    )
//                }
//
//                ProductDetailsContract.Actions.ClickedOnSearch -> {}
//                is ProductDetailsContract.Actions.ClickedAddToCart -> {
//                    addToCart(
//                        action.quantity
//                    )
//                }
//
//                ProductDetailsContract.Actions.Decrement -> {
//                }
//
//                ProductDetailsContract.Actions.Increment -> {
//                }
//            }
//        }
//    }
//
//    private val _states = MutableStateFlow(ProductDetailsContract.States(null))
//    override val states: StateFlow<ProductDetailsContract.States>
//        get() = _states
//    private val _events = MutableSharedFlow<ProductDetailsContract.Events>()
//    override val events: SharedFlow<ProductDetailsContract.Events>
//        get() = _events
//
//    init {
//        viewModelScope.launch {
//            cartManager.count.collect { count ->
//                _states.value = _states.value.copy(
//                    cartItemsCount = count
//                )
//            }
//        }
//    }
//    private var cartQuantity = 0
//    private fun getProductDetails(productId: String?) {
//        viewModelScope.launch {
//            getProductDetailsUseCase.invoke(productId).collect {
//                _states.value = _states.value.copy(productDetails = it)
//                currentProductId = (it as? Result.Success)?.data?.id
//                loadCurrentProductCount()
//            }
//        }
//    }
//    private fun loadCurrentProductCount() {
//        viewModelScope.launch {
//
//            getCartUseCase.invoke().collect { result ->
//
//                if (result is Result.Success) {
//
//                    val item = result.data?.products?.firstOrNull {
//                        it.productId == currentProductId
//                    }
//
//                    cartProductCount = item?.count ?: 0
//
//                    _states.value = _states.value.copy(
//                        quantity = cartProductCount
//                    )
//                }
//
//            }
//        }
//    }
//
//    private fun addToCart(quantity: Int) {
//        val productId =
//            (_states.value.productDetails as? Result.Success)?.data?.id ?: return
//
//        viewModelScope.launch {
//            repeat(quantity) {
//            addToCartUseCase.invoke(productId).collect { result ->
//
//                when (result) {
//
//                    is Result.Success -> {
//
//                        cartManager.updateCount(
//                            result.data?.numOfCartItems ?: 0
//                        )
//
//                        _events.emit(
//                            ProductDetailsContract.Events.ShowMessage(
//                                "Added to cart successfully"
//                            )
//                        )
//                    }
//
//                    is Result.Error -> {
//
//                        _events.emit(
//                            ProductDetailsContract.Events.ShowMessage(
//                                result.failure.message ?: "Something went wrong"
//                            )
//                        )
//
//                    }
//
//                }
//
//            }
//        }
//        }
//    }
//
//}