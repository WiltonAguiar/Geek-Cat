package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class homeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.StartMission1).setOnClickListener {
            val intent = Intent(this, tela_quiz_imagem::class.java)
            startActivity(intent)
    }

        findViewById<ImageView>(R.id.StartMission2).setOnClickListener {
            val intent = Intent(this, tela_quiz_audio::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.imageButton2).setOnClickListener {
            val intent = Intent(this, ShopScreen::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imageButton3).setOnClickListener {
            val intent = Intent(this, ShopScreen::class.java)
            startActivity(intent)
        }


    }}
