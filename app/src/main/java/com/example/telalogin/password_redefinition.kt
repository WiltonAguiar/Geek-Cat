package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class password_redefinition : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_password_redefinition)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botaoRedefenirSenha = findViewById<Button>(R.id.BotaoRedefenirSenha)


            .setOnClickListener {

                AlertDialog.Builder(this)
                    .setTitle("Sucesso!")
                    .setMessage("Senha redefinida com sucesso!")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        findViewById<Button>(R.id.BotaoRedefenirSenha).setOnClickListener {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    .show()
            }

    }
    }
