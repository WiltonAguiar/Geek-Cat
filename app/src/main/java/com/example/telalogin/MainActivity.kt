package com.example.telalogin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var editTextLoginEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var buttonLogin: Button
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var fb: FirebaseFirestore


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        editTextLoginEmail = findViewById(R.id.Email)
        editTextSenha = findViewById(R.id.Senha)
        firebaseAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.registrar).setOnClickListener {
            val intent = Intent(this, register_screen::class.java)
            startActivity(intent)
        }

        // Set OnClickListener para o botão "Esqueceu a Senha"
        findViewById<Button>(R.id.esqueceuSenha).setOnClickListener {
            val intent = Intent(this, forgot_password::class.java)
            startActivity(intent)
        }

        // Set OnClickListener para o botão "Registrar"


        findViewById<Button>(R.id.botaoEntrar).setOnClickListener {
            val intent = Intent(this, user_logged::class.java)
            startActivity(intent)
        }


                }
        }


