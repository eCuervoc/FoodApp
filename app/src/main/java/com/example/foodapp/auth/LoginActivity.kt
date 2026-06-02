package com.example.foodapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.databinding.ActivityLoginBinding
import com.example.foodapp.ui.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val repository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { login() }
        binding.btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            try {
                repository.login(email, password)
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error de login: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
