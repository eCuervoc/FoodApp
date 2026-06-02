package com.example.foodapp.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ItemOrderBinding
import com.example.foodapp.model.Order
import com.example.foodapp.utils.PriceFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private val orders = mutableListOf<Order>()

    fun submitList(newOrders: List<Order>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) = holder.bind(orders[position])

    class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        private val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

        fun bind(order: Order) {
            binding.tvOrderTitle.text = "Pedido: ${order.productName}"
            binding.tvOrderInfo.text = "Cantidad: ${order.quantity} | Total: ${PriceFormatter.pesos(order.total)}"
            binding.tvOrderStatus.text = "Estado: ${order.status} | Pago: ${order.paymentMethod}"
            binding.tvOrderDelivery.text = "Entrega: ${order.deliveryAddress.ifBlank { "Sin dirección" }}"
            binding.tvOrderNote.text = if (order.deliveryNote.isBlank()) "Nota: Sin observaciones" else "Nota: ${order.deliveryNote}"
            binding.tvOrderDate.text = "Fecha: ${formatter.format(Date(order.createdAt))}"
        }
    }
}
