package com.example.foodapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ItemProductBinding
import com.example.foodapp.model.Product

class ProductAdapter(
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var list = listOf<Product>()

    fun submitList(newList: List<Product>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemProductBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]

        holder.binding.name.text = product.name
        holder.binding.price.text = "$${String.format("%,d", product.price.toInt())}"
        holder.binding.description.text = product.description
        holder.binding.image.setImageResource(product.image)

        holder.itemView.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100)
                onClick(product)
            }
        }
    }
}