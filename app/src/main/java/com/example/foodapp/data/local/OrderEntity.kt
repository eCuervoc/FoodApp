package com.example.foodapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapp.model.Order

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val total: Double,
    val status: String,
    val paymentMethod: String,
    val deliveryAddress: String,
    val deliveryNote: String,
    val createdAt: Long
) {
    fun toDomain() = Order(id, userId, productId, productName, quantity, total, status, paymentMethod, deliveryAddress, deliveryNote, createdAt)
}

fun Order.toEntity() = OrderEntity(id, userId, productId, productName, quantity, total, status, paymentMethod, deliveryAddress, deliveryNote, createdAt)
