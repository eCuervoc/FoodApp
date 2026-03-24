package com.example.foodapp.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.ActivityDetailBinding
import com.example.foodapp.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  RECIBIR DATOS
        val name = intent.getStringExtra("name")
        val price = intent.getDoubleExtra("price", 0.0)
        val description = intent.getStringExtra("description")
        val image = intent.getIntExtra("image", 0)

        //  MOSTRAR DATOS
        binding.txtName.text = name
        binding.txtPrice.text = "$${price.toInt()}"
        binding.txtDescription.text = description
        binding.imgProduct.setImageResource(image)

        //  BOTONES
        binding.btnPlus.setOnClickListener {
            viewModel.increase()
        }

        binding.btnMinus.setOnClickListener {
            viewModel.decrease()
        }

        viewModel.quantity.observe(this) {
            binding.txtQuantity.text = it.toString()
        }
    }
}