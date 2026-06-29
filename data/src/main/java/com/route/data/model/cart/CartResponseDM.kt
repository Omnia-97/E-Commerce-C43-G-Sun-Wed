package com.route.data.model.cart

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class CartResponseDM(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("numOfCartItems")
    val numOfCartItems: Int? = null,

    @field:SerializedName("cartId")
    val cartId: String? = null,

    @field:SerializedName("data")
    val cartDataDM: CartDataDM? = null
)

data class CartDataDM(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("cartOwner")
    val cartOwner: String? = null,

    @field:SerializedName("products")
    val products: List<CartProductDM>? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("totalCartPrice")
    val totalCartPrice: Double? = null
)

data class CartProductDM(
    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("product")
    val product: JsonElement? = null,

    @field:SerializedName("price")
    val price: Double? = null
)

data class CartProductDetailsDM(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("imageCover")
    val imageCover: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("ratingsAverage")
    val ratingsAverage: Double? = null,

    @field:SerializedName("category")
    val category: CartProductCategoryDM? = null,

    @field:SerializedName("brand")
    val brand: CartProductBrandDM? = null
)

data class CartProductCategoryDM(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("image")
    val image: String? = null
)

data class CartProductBrandDM(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("image")
    val image: String? = null
)