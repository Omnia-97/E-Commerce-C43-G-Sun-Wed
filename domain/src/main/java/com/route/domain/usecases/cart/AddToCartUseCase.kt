package com.route.domain.usecases.cart

import com.route.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend fun invoke(productId: String) = repository.addProductToCart(productId)
}