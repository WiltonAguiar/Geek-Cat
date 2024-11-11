package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ShopScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_screen)


        findViewById<Button>(R.id.button_sair_loja).setOnClickListener {
            val intent = Intent(this, homeScreen::class.java)
            startActivity(intent)
        }
    }



}