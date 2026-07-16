package com.route.data.mapper.wishlist

import com.route.data.model.wishlist.WishlistItemDM
import com.route.domain.model.wishlist.WishlistItem

fun WishlistItemDM.toDomainWishlistItem(): WishlistItem = WishlistItem(
    id = id,
    title = title,
    price = price,
    imageCover = imageCover,
    brand = brand?.name,
    ratingsAverage = ratingsAverage,
    ratingsQuantity = ratingsQuantity,
    sold = sold,
    quantity = quantity,
    description = description,
    slug = slug
)