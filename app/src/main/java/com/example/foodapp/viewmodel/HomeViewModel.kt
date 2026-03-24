package com.example.foodapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.data.Repository
import com.example.foodapp.model.Product

class HomeViewModel : ViewModel() {

    private val repository = Repository()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun loadProducts() {
        _products.value = repository.getProducts()
    }
}