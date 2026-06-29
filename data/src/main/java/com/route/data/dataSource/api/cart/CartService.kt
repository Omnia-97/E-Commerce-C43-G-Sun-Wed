package com.route.data.dataSource.api.cart

import com.route.data.model.cart.CartResponseDM
import com.route.data.model.cart.ClearCartResponseDM
import com.route.data.model.cart.request.AddToCartRequestParamsDM
import com.route.data.model.cart.request.UpdateCartItemQuantityRequestParamsDM
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartService {
    @POST("cart")
    suspend fun addProductToCart(
        @Body request: AddToCartRequestParamsDM
    ): Response<CartResponseDM>

    @GET("cart")
    suspend fun getLoggedUserCart(): Response<CartResponseDM>

    @PUT("cart/{productId}")
    suspend fun updateCartItemQuantity(
        @Path("productId") productId: String,
        @Body request: UpdateCartItemQuantityRequestParamsDM
    ): Response<CartResponseDM>

    @DELETE("cart/{productId}")
    suspend fun removeCartItem(
        @Path("productId") productId: String
    ): Response<CartResponseDM>

    @DELETE("cart")
    suspend fun clearUserCart(): Response<ClearCartResponseDM>
}