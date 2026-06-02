package com.example.foodapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentProductsBinding
import com.example.foodapp.model.Product
import com.example.foodapp.ui.detail.DetailActivity
import com.example.foodapp.utils.RepositoryProvider
import com.example.foodapp.utils.UiState
import com.example.foodapp.viewmodel.HomeViewModel
import com.example.foodapp.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter
    private var allProducts: List<Product> = emptyList()
    private var selectedCategory: String = "todos"
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(RepositoryProvider.productRepository(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductAdapter { product ->
            startActivity(Intent(requireContext(), DetailActivity::class.java).putExtra("productId", product.id))
        }
        binding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerProducts.adapter = adapter
        binding.btnRetry.setOnClickListener { viewModel.loadProducts() }
        setupCategoryButtons()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collect { state -> render(state) }
        }
        viewModel.loadProducts()
    }

    private fun setupCategoryButtons() {
        val buttons = listOf(
            binding.btnAll to "todos",
            binding.btnBurgers to "hamburguesas",
            binding.btnPizzas to "pizzas",
            binding.btnSalchipapas to "salchipapas",
            binding.btnDogs to "perros",
            binding.btnMazorcadas to "mazorcadas",
            binding.btnCombos to "combos",
            binding.btnDrinks to "bebidas"
        )
        buttons.forEach { (button, category) ->
            button.setOnClickListener {
                selectedCategory = category
                updateCategoryStyle(buttons.map { it.first }, button)
                applyFilter()
            }
        }
        updateCategoryStyle(buttons.map { it.first }, binding.btnAll)
    }

    private fun updateCategoryStyle(buttons: List<Button>, selected: Button) {
        buttons.forEach { it.isSelected = false }
        selected.isSelected = true
    }

    private fun applyFilter() {
        val filtered = if (selectedCategory == "todos") allProducts else allProducts.filter { it.categoryId == selectedCategory }
        adapter.submitList(filtered)
        binding.tvState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        if (filtered.isEmpty()) binding.tvState.text = "No hay productos en esta categoría"
    }

    private fun render(state: UiState<List<Product>>) {
        binding.progress.visibility = if (state is UiState.Loading) View.VISIBLE else View.GONE
        binding.tvState.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
        when (state) {
            is UiState.Success -> {
                allProducts = state.data
                applyFilter()
            }
            is UiState.Empty -> {
                allProducts = emptyList()
                adapter.submitList(emptyList())
                binding.tvState.visibility = View.VISIBLE
                binding.tvState.text = "No hay productos registrados"
            }
            is UiState.Error -> {
                allProducts = emptyList()
                adapter.submitList(emptyList())
                binding.tvState.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.VISIBLE
                binding.tvState.text = state.message
            }
            UiState.Loading -> Unit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
