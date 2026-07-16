package com.route.domain.repository

import com.route.domain.model.Result
import com.route.domain.model.wishlist.WishlistItem
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    suspend fun getWishlist(): Flow<Result<List<WishlistItem>>>
    suspend fun addToWishlist(productId: String): Flow<Result<Set<String>>>
    suspend fun removeFromWishlist(productId: String): Flow<Result<Set<String>>>
}

interface WishlistRemoteDataSource {
    suspend fun getWishlist(): Flow<Result<List<WishlistItem>>>
    suspend fun addToWishlist(productId: String): Flow<Result<Set<String>>>
    suspend fun removeFromWishlist(productId: String): Flow<Result<Set<String>>>
}