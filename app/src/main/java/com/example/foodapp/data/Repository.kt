package com.example.foodapp.data

import com.example.foodapp.R
import com.example.foodapp.model.Product

class Repository {

    fun getProducts(): List<Product> {
        return listOf(
            Product(
                1,
                "Pizza Clásica Italiana",
                28000.0,
                "Pepperoni, queso mozzarella, tomate natural y orégano fresco",
                R.drawable.pizza
            ),
            Product(
                2,
                "BBQ Bacon Master",
                32000.0,
                "Carne angus, salsa BBQ, queso cheddar, tocineta crujiente y cebolla caramelizada",
                R.drawable.burger1
            ),
            Product(
                3,
                "Texana Picante",
                30000.0,
                "Carne angus, queso pepper jack, jalapeños, bacon y salsa chipotle",
                R.drawable.burger2
            )
        )
    }
}