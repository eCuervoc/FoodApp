package com.example.foodapp

import android.app.Application
import com.google.firebase.FirebaseApp

class FoodAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
