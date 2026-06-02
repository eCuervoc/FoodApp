package com.example.foodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.model.Order
import com.example.foodapp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: OrderRepository) : ViewModel() {
    private val _orders = MutableStateFlow<UiState<List<Order>>>(UiState.Loading)
    val orders: StateFlow<UiState<List<Order>>> = _orders

    fun loadOrders(userId: String) {
        _orders.value = UiState.Loading
        viewModelScope.launch {
            repository.getOrders(userId)
                .onSuccess { list -> _orders.value = if (list.isEmpty()) UiState.Empty else UiState.Success(list) }
                .onFailure { _orders.value = UiState.Error(it.message ?: "No fue posible cargar pedidos") }
        }
    }
}
