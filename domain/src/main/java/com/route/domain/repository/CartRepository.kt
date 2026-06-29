package com.route.domain.repository

import com.route.domain.model.Result
import com.route.domain.model.cart.CartData
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(productId: String): Flow<Result<CartData>>
    suspend fun getLoggedUserCart(): Flow<Result<CartData>>
    suspend fun updateCartItemQuantity(productId: String, count: Int): Flow<Result<CartData>>
    suspend fun removeCartItem(productId: String): Flow<Result<CartData>>
    suspend fun clearUserCart(): Flow<Result<Unit>>
}

interface CartRemoteDataSource {
    suspend fun addProductToCart(productId: String): Flow<Result<CartData>>
    suspend fun getLoggedUserCart(): Flow<Result<CartData>>
    suspend fun updateCartItemQuantity(productId: String, count: Int): Flow<Result<CartData>>
    suspend fun removeCartItem(productId: String): Flow<Result<CartData>>
    suspend fun clearUserCart(): Flow<Result<Unit>>
}