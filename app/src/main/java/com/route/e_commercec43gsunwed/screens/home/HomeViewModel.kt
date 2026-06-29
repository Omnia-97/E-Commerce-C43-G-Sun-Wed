package com.route.e_commercec43gsunwed.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.model.Result
import com.route.domain.usecases.cart.GetCartUseCase
import com.route.domain.usecases.category.GetCategoriesUseCase
import com.route.domain.usecases.products.GetProductsUseCase
import com.route.e_commercec43gsunwed.screens.home.composable.home.HomeContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val cartManager: CartManager,
    private val getCartUseCase: GetCartUseCase
) : ViewModel(), HomeContract.ViewModel {
    override fun handleAction(actions: HomeContract.Actions) {
        viewModelScope.launch {
            when (actions) {
                HomeContract.Actions.ClickOnCart -> _events.emit(HomeContract.Events.NavigateToCart)

                is HomeContract.Actions.ClickedOnCategory ->
                    _events.emit(HomeContract.Events.NavigateToSubCategory(actions.category))


                is HomeContract.Actions.ClickedOnProduct -> _events.emit(
                    HomeContract.Events.NavigateToProductDetails(
                        actions.product
                    )
                )

                HomeContract.Actions.ClickedOnSearch -> _events.emit(HomeContract.Events.NavigateToSearch)
                HomeContract.Actions.Idle -> {}
            }
        }
    }

    private val _states = MutableStateFlow<HomeContract.States>(HomeContract.States())
    override val states: StateFlow<HomeContract.States>
        get() = _states

    private val _events = MutableSharedFlow<HomeContract.Events>()
    override val events: SharedFlow<HomeContract.Events>
        get() = _events

    init {
        observeCartCount()

        refreshCart()
    }

    val isLoading = MutableStateFlow(false)
    fun getCategories() {
        viewModelScope.launch {
            isLoading.value = true
            getCategoriesUseCase.invoke().collect {
                isLoading.value = false
                _states.value = _states.value.copy(categories = it)
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            isLoading.value = true
            getProductsUseCase.invoke().collect {
                isLoading.value = false
                _states.value = _states.value.copy(products = it)
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

    private fun refreshCart() {
        viewModelScope.launch {
            cartManager.refreshCartCount()
        }
    }
    // Intent  X
    //     Model View Intent
    //    MVI

    // Add-on UI Architecture
}
//    View -> ViewModel   (Actions/ Intents)
//    ViewModel -> View    States and Events

