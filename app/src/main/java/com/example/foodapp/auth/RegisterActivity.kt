package com.example.foodapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.databinding.ActivityRegisterBinding
import com.example.foodapp.ui.home.HomeActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val repository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { register() }
        binding.btnBackLogin.setOnClickListener { finish() }
    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (email.isBlank() || password.length < 6) {
            Toast.makeText(this, "Correo válido y contraseña mínimo de 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            try {
                repository.register(email, password)
                Toast.makeText(this@RegisterActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error de registro: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
