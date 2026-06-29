package com.route.data.model.cart.request

import com.google.gson.annotations.SerializedName

data class AddToCartRequestParamsDM(
    @field:SerializedName("productId")
    val productId: String? = null
)

data class UpdateCartItemQuantityRequestParamsDM(
    @field:SerializedName("count")
    val count: String? = null
)