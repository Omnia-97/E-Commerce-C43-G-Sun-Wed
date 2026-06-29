package com.route.domain.cart

import com.route.domain.model.Result
import com.route.domain.usecases.cart.GetCartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CartManager @Inject constructor(
    private val getCartUseCase: GetCartUseCase
) {

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    suspend fun refreshCartCount() {
        getCartUseCase.invoke().collect { result ->
            if (result is Result.Success) {
                _count.value = result.data?.numOfCartItems ?: 0
            }
        }
    }

    fun updateCount(count: Int) {
        _count.value = count
    }
}