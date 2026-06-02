package com.example.foodapp.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.databinding.ItemCartBinding
import com.example.foodapp.utils.PriceFormatter
import com.example.foodapp.model.CartItem

class CartAdapter(
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val items = mutableListOf<CartItem>()

    fun submitList(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.tvName.text = item.productName
            binding.tvQuantity.text = item.quantity.toString()
            binding.tvSubtotal.text = PriceFormatter.pesos(item.subtotal)
            binding.ivProduct.setImageResource(productImage(item.imageName))
            binding.root.alpha = 0f
            binding.root.animate().alpha(1f).setDuration(250).start()
            binding.btnMinus.setOnClickListener { onQuantityChanged(item, item.quantity - 1) }
            binding.btnPlus.setOnClickListener { onQuantityChanged(item, item.quantity + 1) }
            binding.btnRemove.setOnClickListener { onRemove(item) }
        }

        private fun productImage(imageName: String): Int {
            val context = binding.root.context
            val imageRes = context.resources.getIdentifier(imageName.ifBlank { "pizza_pepperoni" }, "drawable", context.packageName)
            return if (imageRes != 0) imageRes else R.drawable.pizza_pepperoni
        }
    }
}
