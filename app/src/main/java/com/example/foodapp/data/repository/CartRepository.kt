package com.example.foodapp.data.repository

import com.example.foodapp.data.local.CartDao
import com.example.foodapp.data.local.toEntity
import com.example.foodapp.model.CartItem
import com.example.foodapp.model.Product

class CartRepository(
    private val cartDao: CartDao
) {
    suspend fun getItems(): List<CartItem> = cartDao.getCartItems().map { it.toDomain() }

    suspend fun addProduct(product: Product, quantity: Int) {
        val current = cartDao.getCartItem(product.id)
        if (current == null) {
            cartDao.saveCartItem(
                CartItem(
                    productId = product.id,
                    productName = product.name,
                    description = product.description,
                    price = product.price,
                    quantity = quantity.coerceAtLeast(1),
                    imageName = product.imageName
                ).toEntity()
            )
        } else {
            cartDao.updateQuantity(product.id, current.quantity + quantity.coerceAtLeast(1))
        }
    }

    suspend fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) cartDao.deleteCartItem(productId) else cartDao.updateQuantity(productId, quantity)
    }

    suspend fun remove(productId: String) = cartDao.deleteCartItem(productId)

    suspend fun clear() = cartDao.clearCart()
}
