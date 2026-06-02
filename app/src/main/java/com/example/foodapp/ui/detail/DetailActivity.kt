package com.example.foodapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.data.local.AppDatabase
import com.example.foodapp.databinding.ActivityDetailBinding
import com.example.foodapp.ui.cart.CartActivity
import com.example.foodapp.ui.home.HomeActivity
import com.example.foodapp.utils.RepositoryProvider
import com.example.foodapp.utils.PriceFormatter
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
        binding.btnGoHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
            finish()
        }

        val productId = intent.getStringExtra("productId") ?: return finish()
        loadProduct(productId)
    }

    private fun loadProduct(productId: String) {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@DetailActivity)
            val product = db.productDao().getProductById(productId)?.toDomain()
            if (product == null) {
                Toast.makeText(this@DetailActivity, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            binding.tvName.text = product.name
            binding.tvDescription.text = product.description.ifBlank { "Producto fresco preparado al momento con ingredientes seleccionados." }
            binding.tvPrice.text = PriceFormatter.pesos(product.price)
            binding.tvCategory.text = categoryLabel(product.categoryId)
            binding.tvTag.text = product.tag.ifBlank { "Disponible" }
            binding.ivProduct.setImageResource(productImage(product.imageName))
            binding.ivProduct.alpha = 0f
            binding.ivProduct.animate().alpha(1f).setDuration(350).start()

            binding.btnMinus.setOnClickListener {
                val q = binding.tvQuantity.text.toString().toInt().coerceAtLeast(1)
                binding.tvQuantity.text = (q - 1).coerceAtLeast(1).toString()
            }
            binding.btnPlus.setOnClickListener {
                val q = binding.tvQuantity.text.toString().toInt().coerceAtLeast(1)
                binding.tvQuantity.text = (q + 1).toString()
            }

            binding.btnCreateOrder.text = "Agregar al carrito"
            binding.btnCreateOrder.setOnClickListener {
                val quantity = binding.tvQuantity.text.toString().toInt().coerceAtLeast(1)
                lifecycleScope.launch {
                    RepositoryProvider.cartRepository(this@DetailActivity).addProduct(product, quantity)
                    Toast.makeText(this@DetailActivity, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@DetailActivity, CartActivity::class.java))
                }
            }
        }
    }

    private fun productImage(imageName: String): Int {
        val imageRes = resources.getIdentifier(imageName.ifBlank { "pizza_pepperoni" }, "drawable", packageName)
        return if (imageRes != 0) imageRes else R.drawable.pizza_pepperoni
    }

    private fun categoryLabel(categoryId: String): String = when (categoryId) {
        "hamburguesas" -> "🍔 Hamburguesas"
        "pizzas" -> "🍕 Pizzas"
        "salchipapas" -> "🍟 Salchipapas"
        "perros" -> "🌭 Perros calientes"
        "mazorcadas" -> "🌽 Mazorcadas"
        "combos" -> "🥤 Combos"
        "bebidas" -> "🧃 Bebidas"
        else -> categoryId.replaceFirstChar { it.uppercase() }
    }
}
