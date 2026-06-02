package com.example.foodapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapp.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val categoryId: String,
    val imageName: String,
    val tag: String
) {
    fun toDomain() = Product(id, name, description, price, categoryId, imageName, tag)
}

fun Product.toEntity() = ProductEntity(id, name, description, price, categoryId, imageName, tag)
