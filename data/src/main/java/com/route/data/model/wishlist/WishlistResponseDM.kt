package com.route.data.model.wishlist

import com.google.gson.annotations.SerializedName

data class WishlistResponseDM(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("data")
    val data: List<WishlistItemDM>? = null
)

data class WishlistItemDM(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("imageCover")
    val imageCover: String? = null,

    @field:SerializedName("brand")
    val brand: WishlistItemBrandDM? = null,

    @field:SerializedName("ratingsAverage")
    val ratingsAverage: Double? = null,

    @field:SerializedName("ratingsQuantity")
    val ratingsQuantity: Int? = null,

    @field:SerializedName("sold")
    val sold: Int? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null
)

data class WishlistItemBrandDM(
    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null
)


data class WishlistMutationResponseDM(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<String>? = null
)