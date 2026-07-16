package com.route.domain.usecases.wishlist

import com.route.domain.repository.WishlistRepository
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    suspend fun invoke() = repository.getWishlist()
}