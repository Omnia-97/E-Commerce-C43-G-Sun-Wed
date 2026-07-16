package com.route.domain.usecases.wishlist

import com.route.domain.repository.WishlistRepository
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    suspend fun invoke(productId: String) = repository.removeFromWishlist(productId)
}