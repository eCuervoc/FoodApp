package com.example.foodapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY productName ASC")
    suspend fun getCartItems(): List<CartEntity>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getCartItem(productId: String): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCartItem(item: CartEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
