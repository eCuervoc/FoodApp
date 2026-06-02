package com.example.foodapp.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val categoryId: String = "",
    val imageName: String = "pizza",
    val tag: String = ""
)
