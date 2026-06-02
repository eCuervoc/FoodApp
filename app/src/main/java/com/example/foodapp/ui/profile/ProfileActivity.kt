package com.example.foodapp.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.auth.AuthRepository
import com.example.foodapp.auth.LoginActivity
import com.example.foodapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvEmail.text = FirebaseAuth.getInstance().currentUser?.email ?: "Usuario sin correo"
        binding.btnLogout.setOnClickListener {
            authRepository.logout()
            startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
        }
        binding.btnBack.setOnClickListener { finish() }
    }
}
