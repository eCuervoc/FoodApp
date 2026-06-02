package com.example.foodapp.ui.cart

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.auth.AuthRepository
import com.example.foodapp.databinding.ActivityCartBinding
import com.example.foodapp.model.CartItem
import com.example.foodapp.model.Order
import com.example.foodapp.utils.PriceFormatter
import com.example.foodapp.utils.RepositoryProvider
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CartAdapter(
            onQuantityChanged = { item, quantity -> updateQuantity(item, quantity) },
            onRemove = { item -> removeItem(item) }
        )
        binding.recyclerCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.adapter = adapter
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCheckout.setOnClickListener { showConfirmDialog() }
        loadCart()
    }

    override fun onResume() {
        super.onResume()
        loadCart()
    }

    private fun loadCart() {
        lifecycleScope.launch {
            val items = RepositoryProvider.cartRepository(this@CartActivity).getItems()
            render(items)
        }
    }

    private fun render(items: List<CartItem>) {
        adapter.submitList(items)
        val total = items.sumOf { it.subtotal }
        binding.tvTotal.text = "Total: ${PriceFormatter.pesos(total)}"
        binding.tvSummary.text = if (items.isEmpty()) "0 productos" else "${items.sumOf { it.quantity }} productos seleccionados"
        val isEmpty = items.isEmpty()
        binding.tvCartMessage.text = if (isEmpty) {
            "Tu carrito está vacío. Vuelve al catálogo y agrega hamburguesas, pizzas o salchipapas."
        } else {
            "Revisa cantidades, escribe tu dirección, elige el método de pago y confirma."
        }
        binding.recyclerCart.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.deliverySection.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.paymentSection.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.btnCheckout.isEnabled = !isEmpty
    }

    private fun updateQuantity(item: CartItem, quantity: Int) {
        lifecycleScope.launch {
            RepositoryProvider.cartRepository(this@CartActivity).updateQuantity(item.productId, quantity)
            loadCart()
        }
    }

    private fun removeItem(item: CartItem) {
        lifecycleScope.launch {
            RepositoryProvider.cartRepository(this@CartActivity).remove(item.productId)
            Toast.makeText(this@CartActivity, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
            loadCart()
        }
    }

    private fun selectedPaymentMethod(): String = if (binding.rbCard.isChecked) "Tarjeta" else "Efectivo"

    private fun showConfirmDialog() {
        val address = binding.etAddress.text.toString().trim()
        if (address.isBlank()) {
            binding.etAddress.error = "Escribe la dirección de entrega"
            return
        }
        lifecycleScope.launch {
            val items = RepositoryProvider.cartRepository(this@CartActivity).getItems()
            val total = items.sumOf { it.subtotal }
            AlertDialog.Builder(this@CartActivity)
                .setTitle("¿Confirmar pedido?")
                .setMessage("Total: ${PriceFormatter.pesos(total)}\nPago: ${selectedPaymentMethod()}\nDirección: $address")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar") { _, _ -> checkout() }
                .show()
        }
    }

    private fun checkout() {
        val uid = authRepository.currentUserId
        if (uid == null) {
            Toast.makeText(this, "Debes iniciar sesión para hacer el pedido", Toast.LENGTH_SHORT).show()
            return
        }
        val address = binding.etAddress.text.toString().trim()
        val note = binding.etNote.text.toString().trim()
        if (address.isBlank()) {
            binding.etAddress.error = "Escribe la dirección de entrega"
            return
        }
        lifecycleScope.launch {
            val cartRepository = RepositoryProvider.cartRepository(this@CartActivity)
            val orderRepository = RepositoryProvider.orderRepository(this@CartActivity)
            val items = cartRepository.getItems()
            if (items.isEmpty()) {
                Toast.makeText(this@CartActivity, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val summary = items.joinToString(separator = " + ") { "${it.quantity}x ${it.productName}" }
            val order = Order(
                userId = uid,
                productId = "cart",
                productName = summary,
                quantity = items.sumOf { it.quantity },
                total = items.sumOf { it.subtotal },
                status = "Pendiente",
                paymentMethod = selectedPaymentMethod(),
                deliveryAddress = address,
                deliveryNote = note
            )
            val result = orderRepository.createOrder(order)
            if (result.isFailure) {
                Toast.makeText(this@CartActivity, "No se pudo registrar el pedido. Revisa tu conexión.", Toast.LENGTH_LONG).show()
            } else {
                cartRepository.clear()
                Toast.makeText(this@CartActivity, "Pedido realizado con éxito", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
