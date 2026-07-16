package com.route.data.dataSource.wishlist

import com.route.data.dataSource.api.wishlist.WishlistService
import com.route.data.dataSource.utils.safeApiCall
import com.route.data.mapper.wishlist.toDomainWishlistItem
import com.route.domain.model.Result
import com.route.domain.model.wishlist.WishlistItem
import com.route.domain.repository.WishlistRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistRemoteDataSourceImpl @Inject constructor(
    private val service: WishlistService
) : WishlistRemoteDataSource {

    override suspend fun getWishlist(): Flow<Result<List<WishlistItem>>> =
        safeApiCall(
            apiCall = { service.getWishlist() },
            mapper = { response ->
                response.data?.map { it.toDomainWishlistItem() } ?: emptyList()
            }
        )

    override suspend fun addToWishlist(productId: String): Flow<Result<Set<String>>> =
        safeApiCall(
            apiCall = { service.addToWishlist(mapOf("productId" to productId)) },
            mapper = { response ->
                response.data?.toSet() ?: emptySet()
            }
        )

    override suspend fun removeFromWishlist(productId: String): Flow<Result<Set<String>>> =
        safeApiCall(
            apiCall = { service.removeFromWishlist(productId) },
            mapper = { response ->
                response.data?.toSet() ?: emptySet()
            }
        )
}