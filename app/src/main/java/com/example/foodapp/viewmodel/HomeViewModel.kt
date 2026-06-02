package com.example.foodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.repository.ProductRepository
import com.example.foodapp.model.Product
import com.example.foodapp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val products: StateFlow<UiState<List<Product>>> = _products

    fun loadProducts() {
        _products.value = UiState.Loading
        viewModelScope.launch {
            repository.getProducts()
                .onSuccess { list -> _products.value = if (list.isEmpty()) UiState.Empty else UiState.Success(list) }
                .onFailure { _products.value = UiState.Error(it.message ?: "No fue posible cargar productos") }
        }
    }
}
