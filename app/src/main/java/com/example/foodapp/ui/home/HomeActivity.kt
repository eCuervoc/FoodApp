package com.example.foodapp.ui.home

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHomeBinding
import com.example.foodapp.ui.cart.CartFragment
import com.example.foodapp.ui.orders.OrdersFragment
import com.example.foodapp.ui.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenMenu.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }
        binding.btnHomeTop.setOnClickListener { goHome() }

        binding.btnProducts.setOnClickListener { goHome() }
        binding.btnCart.setOnClickListener { openFragment(CartFragment(), "Carrito de compra") }
        binding.btnOrders.setOnClickListener { openFragment(OrdersFragment(), "Mis pedidos") }
        binding.btnProfile.setOnClickListener { openFragment(ProfileFragment(), "Mi perfil") }

        binding.menuInicio.setOnClickListener { goHome() }
        binding.menuProductos.setOnClickListener { goHome() }
        binding.menuCarrito.setOnClickListener { openFragment(CartFragment(), "Carrito de compra") }
        binding.menuPedidos.setOnClickListener { openFragment(OrdersFragment(), "Historial de pedidos") }
        binding.menuPerfil.setOnClickListener { openFragment(ProfileFragment(), "Mi perfil") }

        if (savedInstanceState == null) {
            goHome()
        }
    }

    fun goHome() {
        openFragment(ProductsFragment(), "FoodApp Pedidos")
    }

    private fun openFragment(fragment: Fragment, title: String) {
        binding.tvToolbarTitle.text = title
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}
