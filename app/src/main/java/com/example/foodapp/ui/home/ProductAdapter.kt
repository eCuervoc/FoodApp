package com.example.foodapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.databinding.ItemProductBinding
import com.example.foodapp.model.Product
import com.example.foodapp.utils.PriceFormatter

class ProductAdapter(
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()

    fun submitList(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvName.text = product.name
            binding.tvDescription.text = product.description.ifBlank { "Producto fresco preparado al momento con ingredientes seleccionados." }
            binding.tvPrice.text = PriceFormatter.pesos(product.price)
            binding.tvCategory.text = categoryLabel(product.categoryId)
            binding.tvTag.text = product.tag.ifBlank { "Disponible" }
            binding.tvTag.visibility = if (product.tag.isBlank()) View.GONE else View.VISIBLE
            binding.btnAdd.text = "Ver y agregar"

            val context = binding.root.context
            val imageName = product.imageName.ifBlank { "pizza_pepperoni" }
            val imageRes = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            binding.ivProduct.setImageResource(if (imageRes != 0) imageRes else R.drawable.pizza_pepperoni)

            binding.root.alpha = 0f
            binding.root.translationY = 24f
            binding.root.animate().alpha(1f).translationY(0f).setDuration(280).start()

            binding.root.setOnClickListener { onClick(product) }
            binding.btnAdd.setOnClickListener { onClick(product) }
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
}
