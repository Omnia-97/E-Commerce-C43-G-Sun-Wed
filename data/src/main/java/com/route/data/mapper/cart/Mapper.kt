package com.route.data.mapper.cart

import com.google.gson.Gson
import com.route.data.model.cart.CartDataDM
import com.route.data.model.cart.CartProductDM
import com.route.data.model.cart.CartProductDetailsDM
import com.route.domain.model.cart.CartData
import com.route.domain.model.cart.CartProductItem

private val gson = Gson()

fun CartDataDM.toDomainCartData(
    numOfCartItems: Int? = null,
    cartId: String? = null
): CartData {
    return CartData(
        id = id,
        cartOwner = cartOwner,
        products = products?.map { it.toDomainCartProductItem() } ?: listOf(),
        totalCartPrice = totalCartPrice,
        numOfCartItems = numOfCartItems,
        cartId = cartId ?: id,
        updatedAt = updatedAt
    )
}

fun CartProductDM.toDomainCartProductItem(): CartProductItem {
    val productElement = product
    val productDetails: CartProductDetailsDM? = if (productElement?.isJsonObject == true) {
        gson.fromJson(productElement, CartProductDetailsDM::class.java)
    } else null
    val productIdFromPrimitive = if (productElement?.isJsonPrimitive == true) {
        productElement.asString
    } else null

    return CartProductItem(
        cartItemId = id,
        productId = productDetails?.id ?: productIdFromPrimitive,
        title = productDetails?.title,
        imageCover = productDetails?.imageCover,
        brandName = productDetails?.brand?.name,
        categoryName = productDetails?.category?.name,
        availableQuantity = productDetails?.quantity,
        price = price,
        count = count
    )
}