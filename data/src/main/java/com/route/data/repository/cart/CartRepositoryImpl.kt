package com.route.data.repository.cart

import com.route.domain.model.Result
import com.route.domain.model.cart.CartData
import com.route.domain.repository.CartRemoteDataSource
import com.route.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val remoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun addProductToCart(productId: String): Flow<Result<CartData>> =
        remoteDataSource.addProductToCart(productId)

    override suspend fun getLoggedUserCart(): Flow<Result<CartData>> =
        remoteDataSource.getLoggedUserCart()

    override suspend fun updateCartItemQuantity(
        productId: String,
        count: Int
    ): Flow<Result<CartData>> =
        remoteDataSource.updateCartItemQuantity(productId, count)

    override suspend fun removeCartItem(productId: String): Flow<Result<CartData>> =
        remoteDataSource.removeCartItem(productId)

    override suspend fun clearUserCart(): Flow<Result<Unit>> =
        remoteDataSource.clearUserCart()
}