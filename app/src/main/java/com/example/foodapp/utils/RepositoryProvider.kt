package com.example.foodapp.utils

import android.content.Context
import com.example.foodapp.crash.CrashLogger
import com.example.foodapp.data.local.AppDatabase
import com.example.foodapp.data.remote.FirestoreDataSource
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.ProductRepository

object RepositoryProvider {
    fun productRepository(context: Context): ProductRepository {
        val db = AppDatabase.getInstance(context)
        return ProductRepository(db.productDao(), FirestoreDataSource(), CrashLogger())
    }

    fun orderRepository(context: Context): OrderRepository {
        val db = AppDatabase.getInstance(context)
        return OrderRepository(db.orderDao(), FirestoreDataSource(), CrashLogger())
    }

    fun cartRepository(context: Context): CartRepository {
        val db = AppDatabase.getInstance(context)
        return CartRepository(db.cartDao())
    }
}
