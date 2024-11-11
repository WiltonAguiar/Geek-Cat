package com.example.telalogin

import android.content.Intent
import android.os.Bundle

import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class level_creator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level_creator)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val botaoFinalizarCriacaoFase = findViewById<Button>(R.id.BotaoFinalizarCriacaoFase)


        botaoFinalizarCriacaoFase.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Sucesso!")
                .setMessage("Fase criada com sucesso!")
                .setPositiveButton("OK") { dialog, _ ->

                    dialog.dismiss()
                    val intent = Intent(this, user_logged::class.java)
                    startActivity(intent)
                }
                .show()
        }

    }
}
