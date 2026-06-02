package com.example.foodapp.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.auth.AuthRepository
import com.example.foodapp.databinding.FragmentOrdersBinding
import com.example.foodapp.model.Order
import com.example.foodapp.utils.RepositoryProvider
import com.example.foodapp.utils.UiState
import com.example.foodapp.viewmodel.OrdersViewModel
import com.example.foodapp.viewmodel.OrdersViewModelFactory
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val adapter = OrderAdapter()
    private val authRepository = AuthRepository()
    private val viewModel: OrdersViewModel by viewModels {
        OrdersViewModelFactory(RepositoryProvider.orderRepository(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerOrders.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch { viewModel.orders.collect { render(it) } }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
