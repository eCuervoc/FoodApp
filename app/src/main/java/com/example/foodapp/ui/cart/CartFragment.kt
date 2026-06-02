package com.example.foodapp.ui.cart

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.auth.AuthRepository
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.model.CartItem
import com.example.foodapp.model.Order
import com.example.foodapp.utils.NetworkUtils
import com.example.foodapp.utils.PriceFormatter
import com.example.foodapp.utils.RepositoryProvider
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartAdapter
    private val authRepository = AuthRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CartAdapter(
            onQuantityChanged = { item, quantity -> updateQuantity(item, quantity) },
            onRemove = { item -> removeItem(item) }
        )
        binding.recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCart.adapter = adapter
        binding.btnBack.visibility = View.GONE
        binding.btnCheckout.setOnClickListener { showConfirmDialog() }
        loadCart()
    }

    override fun onResume() {
        super.onResume()
        loadCart()
    }

    private fun loadCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            val items = RepositoryProvider.cartRepository(requireContext()).getItems()
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
        viewLifecycleOwner.lifecycleScope.launch {
            RepositoryProvider.cartRepository(requireContext()).updateQuantity(item.productId, quantity)
            loadCart()
        }
    }

    private fun removeItem(item: CartItem) {
        viewLifecycleOwner.lifecycleScope.launch {
            RepositoryProvider.cartRepository(requireContext()).remove(item.productId)
            Toast.makeText(requireContext(), "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
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
        viewLifecycleOwner.lifecycleScope.launch {
            val items = RepositoryProvider.cartRepository(requireContext()).getItems()
            val total = items.sumOf { it.subtotal }
            AlertDialog.Builder(requireContext())
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
            Toast.makeText(requireContext(), "Debes iniciar sesión para hacer el pedido", Toast.LENGTH_SHORT).show()
            return
        }

        val address = binding.etAddress.text.toString().trim()
        val note = binding.etNote.text.toString().trim()
        if (address.isBlank()) {
            binding.etAddress.error = "Escribe la dirección de entrega"
            return
        }

        if (!NetworkUtils.isOnline(requireContext())) {
            Toast.makeText(
                requireContext(),
                "No tienes conexión a internet. Conéctate para confirmar el pedido.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val cartRepository = RepositoryProvider.cartRepository(requireContext())
            val orderRepository = RepositoryProvider.orderRepository(requireContext())
            val items = cartRepository.getItems()
            if (items.isEmpty()) {
                Toast.makeText(requireContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "No se pudo registrar el pedido. Revisa tu conexión.", Toast.LENGTH_LONG).show()
            } else {
                cartRepository.clear()
                binding.etAddress.text?.clear()
                binding.etNote.text?.clear()
                Toast.makeText(requireContext(), "Pedido realizado con éxito", Toast.LENGTH_LONG).show()
                loadCart()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
