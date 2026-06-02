package com.example.foodapp.ui.orders

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.auth.AuthRepository
import com.example.foodapp.databinding.ActivityOrdersBinding
import com.example.foodapp.model.Order
import com.example.foodapp.utils.RepositoryProvider
import com.example.foodapp.utils.UiState
import com.example.foodapp.viewmodel.OrdersViewModel
import com.example.foodapp.viewmodel.OrdersViewModelFactory
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private val adapter = OrderAdapter()
    private val authRepository = AuthRepository()
    private val viewModel: OrdersViewModel by viewModels {
        OrdersViewModelFactory(RepositoryProvider.orderRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerOrders.adapter = adapter
        binding.btnBack.setOnClickListener { finish() }

        lifecycleScope.launch { viewModel.orders.collect { render(it) } }
        val uid = authRepository.currentUserId
        if (uid == null) {
            binding.tvState.text = "Sesión no autenticada"
            binding.tvState.visibility = View.VISIBLE
        } else {
            viewModel.loadOrders(uid)
        }
    }

    private fun render(state: UiState<List<Order>>) {
        binding.progress.visibility = if (state is UiState.Loading) View.VISIBLE else View.GONE
        binding.tvState.visibility = View.GONE
        when (state) {
            is UiState.Success -> adapter.submitList(state.data)
            is UiState.Empty -> {
                adapter.submitList(emptyList())
                binding.tvState.text = "Todavía no tienes pedidos"
                binding.tvState.visibility = View.VISIBLE
            }
            is UiState.Error -> {
                adapter.submitList(emptyList())
                binding.tvState.text = state.message
                binding.tvState.visibility = View.VISIBLE
            }
            UiState.Loading -> Unit
        }
    }
}
