package com.route.data.dataSource.api.wishlist

import com.route.data.model.wishlist.WishlistMutationResponseDM
import com.route.data.model.wishlist.WishlistResponseDM
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WishlistService {
    @GET("wishlist")
    suspend fun getWishlist(): Response<WishlistResponseDM>

    @POST("wishlist")
    suspend fun addToWishlist(
        @Body body: Map<String, String>
    ): Response<WishlistMutationResponseDM>

    @DELETE("wishlist/{productId}")
    suspend fun removeFromWishlist(
        @Path("productId") productId: String
    ): Response<WishlistMutationResponseDM>
}