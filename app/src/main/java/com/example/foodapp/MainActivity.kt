package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.auth.LoginActivity
import com.example.foodapp.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destination = if (FirebaseAuth.getInstance().currentUser != null) {
            HomeActivity::class.java
        } else {
            LoginActivity::class.java
        }
        startActivity(Intent(this, destination))
        finish()
    }
}
