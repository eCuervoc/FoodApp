package com.example.foodapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.ActivityHomeBinding
import com.example.foodapp.viewmodel.HomeViewModel
import com.example.foodapp.ui.home.ProductAdapter
import com.example.foodapp.ui.detail.DetailActivity
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ProductAdapter { product ->

            val intent = Intent(this, DetailActivity::class.java)

            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("description", product.description)
            intent.putExtra("image", product.image)

            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.products.observe(this) {
            adapter.submitList(it)
        }

        viewModel.loadProducts()
    }
}