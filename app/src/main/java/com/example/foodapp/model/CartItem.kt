package com.example.foodapp.model

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val imageName: String = "pizza"
) {
    val subtotal: Double
        get() = price * quantity
}
