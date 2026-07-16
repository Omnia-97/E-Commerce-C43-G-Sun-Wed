package com.route.domain.wishlist

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistManager @Inject constructor() {

    private val _wishlistIds = MutableStateFlow<Set<String>>(emptySet())
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds

    fun update(ids: Set<String>) {
        _wishlistIds.value = ids
    }

    fun isInWishlist(productId: String): Boolean {
        return _wishlistIds.value.contains(productId)
    }
}