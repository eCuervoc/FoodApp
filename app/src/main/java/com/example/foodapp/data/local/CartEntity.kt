package com.example.foodapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapp.model.CartItem

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val productId: String,
    val productName: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imageName: String
) {
    fun toDomain() = CartItem(productId, productName, description, price, quantity, imageName)
}

fun CartItem.toEntity() = CartEntity(productId, productName, description, price, quantity, imageName)
