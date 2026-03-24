package com.example.foodapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {

    val quantity = MutableLiveData(1)

    fun increase() {
        quantity.value = (quantity.value ?: 1) + 1
    }

    fun decrease() {
        if ((quantity.value ?: 1) > 1) {
            quantity.value = (quantity.value ?: 1) - 1
        }
    }
}