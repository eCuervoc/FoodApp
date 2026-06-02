package com.example.foodapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getOrdersByUser(userId: String): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrders(orders: List<OrderEntity>)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)
}
