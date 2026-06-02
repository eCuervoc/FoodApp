package com.example.foodapp.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 1,
    val total: Double = 0.0,
    val status: String = "Pendiente",
    val paymentMethod: String = "Efectivo",
    val deliveryAddress: String = "",
    val deliveryNote: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
