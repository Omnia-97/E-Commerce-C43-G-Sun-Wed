package com.route.data.dataSource.cart

import com.route.data.dataSource.api.cart.CartService
import com.route.data.dataSource.utils.safeApiCall
import com.route.data.mapper.cart.toDomainCartData
import com.route.data.model.cart.request.AddToCartRequestParamsDM
import com.route.data.model.cart.request.UpdateCartItemQuantityRequestParamsDM
import com.route.domain.model.Result
import com.route.domain.model.cart.CartData
import com.route.domain.repository.CartRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRemoteDataSourceImpl @Inject constructor(
    private val service: CartService
) : CartRemoteDataSource {
    override suspend fun addProductToCart(productId: String): Flow<Result<CartData>> =
        safeApiCall(apiCall = {
            service.addProductToCart(AddToCartRequestParamsDM(productId = productId))
        }, mapper = {
            it.cartDataDM?.toDomainCartData(it.numOfCartItems, it.cartId) ?: CartData()
        })

    override suspend fun getLoggedUserCart(): Flow<Result<CartData>> =
        safeApiCall(apiCall = {
            service.getLoggedUserCart()
        }, mapper = {
            it.cartDataDM?.toDomainCartData(it.numOfCartItems, it.cartId) ?: CartData()
        })

    override suspend fun updateCartItemQuantity(
        productId: String,
        count: Int
    ): Flow<Result<CartData>> =
        safeApiCall(apiCall = {
            service.updateCartItemQuantity(
                productId = productId,
                request = UpdateCartItemQuantityRequestParamsDM(count = count.toString())
            )
        }, mapper = {
            it.cartDataDM?.toDomainCartData(it.numOfCartItems, it.cartId) ?: CartData()
        })

    override suspend fun removeCartItem(productId: String): Flow<Result<CartData>> =
        safeApiCall(apiCall = {
            service.removeCartItem(productId = productId)
        }, mapper = {
            it.cartDataDM?.toDomainCartData(it.numOfCartItems, it.cartId) ?: CartData()
        })

    override suspend fun clearUserCart(): Flow<Result<Unit>> =
        safeApiCall(apiCall = {
            service.clearUserCart()
        }, mapper = {
            Unit
        })
}