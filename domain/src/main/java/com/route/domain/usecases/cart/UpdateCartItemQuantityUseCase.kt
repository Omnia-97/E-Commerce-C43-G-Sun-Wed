package com.route.domain.usecases.cart

import com.route.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend fun invoke(productId: String, count: Int) =
        repository.updateCartItemQuantity(productId, count)
}