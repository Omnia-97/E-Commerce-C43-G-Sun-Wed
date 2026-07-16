package com.route.data.repository.wishlist

import com.route.domain.model.Result
import com.route.domain.model.wishlist.WishlistItem
import com.route.domain.repository.WishlistRemoteDataSource
import com.route.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val remoteDataSource: WishlistRemoteDataSource
) : WishlistRepository {

    override suspend fun getWishlist(): Flow<Result<List<WishlistItem>>> =
        remoteDataSource.getWishlist()

    override suspend fun addToWishlist(productId: String): Flow<Result<Set<String>>> =
        remoteDataSource.addToWishlist(productId)

    override suspend fun removeFromWishlist(productId: String): Flow<Result<Set<String>>> =
        remoteDataSource.removeFromWishlist(productId)
}