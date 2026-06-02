package com.example.foodapp.data.repository

import com.example.foodapp.crash.CrashLogger
import com.example.foodapp.data.local.OrderDao
import com.example.foodapp.data.local.toEntity
import com.example.foodapp.data.remote.FirestoreDataSource
import com.example.foodapp.model.Order

class OrderRepository(
    private val orderDao: OrderDao,
    private val remote: FirestoreDataSource,
    private val crashLogger: CrashLogger
) {
    suspend fun createOrder(order: Order): Result<Order> {
        return try {
            orderDao.saveOrder(order.toEntity())
            val remoteOrder = remote.createOrder(order)
            orderDao.saveOrder(remoteOrder.toEntity())
            Result.success(remoteOrder)
        } catch (e: Exception) {
            crashLogger.logNonFatalError("OrderRepository.createOrder", e)
            Result.failure(e)
        }
    }

    suspend fun getOrders(userId: String): Result<List<Order>> {
        val localOrders = orderDao.getOrdersByUser(userId).map { it.toDomain() }
        return try {
            val remoteOrders = remote.getOrders(userId)
            orderDao.saveOrders(remoteOrders.map { it.toEntity() })
            Result.success(remoteOrders)
        } catch (e: Exception) {
            crashLogger.logNonFatalError("OrderRepository.getOrders", e)
            if (localOrders.isNotEmpty()) Result.success(localOrders) else Result.failure(e)
        }
    }
}
