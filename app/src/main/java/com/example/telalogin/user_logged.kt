package com.example.telalogin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class user_logged : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_logged)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var aux = intent.getStringExtra("id")
        findViewById<Button>(R.id.botaocriarfase
        ).setOnClickListener {
            val intent = Intent(this, level_creator::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.botaosair
        ).setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.botaoFase
        ).setOnClickListener {
            val intent = Intent(this, RecyclerViewExemplo::class.java)
            startActivity(intent)
        }



    }
}