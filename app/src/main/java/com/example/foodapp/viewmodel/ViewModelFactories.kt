package com.example.foodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.ProductRepository

class HomeViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel no reconocido")
    }
}

class OrdersViewModelFactory(private val repository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return OrdersViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel no reconocido")
    }
}
