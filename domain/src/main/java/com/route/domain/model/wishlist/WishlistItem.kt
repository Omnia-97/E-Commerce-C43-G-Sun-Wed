package com.route.domain.model.wishlist

data class WishlistItem(
    val id: String? = null,
    val title: String? = null,
    val price: Int? = null,
    val imageCover: String? = null,
    val brand: String? = null,
    val ratingsAverage: Double? = null,
    val ratingsQuantity: Int? = null,
    val sold: Int? = null,
    val quantity: Int? = null,
    val description: String? = null,
    val slug: String? = null
)