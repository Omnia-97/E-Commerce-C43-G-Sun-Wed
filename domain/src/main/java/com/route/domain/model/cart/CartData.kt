package com.route.domain.model.cart

data class CartData(
    val id: String? = null,
    val cartOwner: String? = null,
    val products: List<CartProductItem> = listOf(),
    val totalCartPrice: Double? = null,
    val numOfCartItems: Int? = null,
    val cartId: String? = null,
    val updatedAt: String? = null
)

data class CartProductItem(
    val cartItemId: String? = null,
    val productId: String? = null,
    val title: String? = null,
    val imageCover: String? = null,
    val brandName: String? = null,
    val categoryName: String? = null,
    val availableQuantity: Int? = null,
    val price: Double? = null,
    val count: Int? = null
)