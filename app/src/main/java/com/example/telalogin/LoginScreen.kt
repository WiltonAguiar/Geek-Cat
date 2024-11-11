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

class LoginScreen : AppCompatActivity() {

    private lateinit var editTextLoginEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var buttonLogin: Button
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
//
//        editTextLoginEmail = findViewById(R.id.Email)
//        editTextSenha = findViewById(R.id.Senha)
//        buttonLogin = findViewById(R.id.botaoEntrar)
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        buttonLogin.setOnClickListener {
//            val email = editTextLoginEmail.text.toString().trim()
//            val senha = editTextSenha.text.toString().trim()
//
//            findViewById<Button>(R.id.esqueceuSenha).setOnClickListener {
//                val intent = Intent(this, forgot_password::class.java)
//                startActivity(intent)
//            }
//
//            findViewById<Button>(R.id.registrar).setOnClickListener {
//                val intent = Intent(this, register_screen::class.java)
//                startActivity(intent)
//            }
//
//            // Validações básicas para e-mail e senha
//            if (email.isEmpty() || senha.isEmpty()) {
//                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Autenticação com o Firebase Authentication
//            firebaseAuth.signInWithEmailAndPassword(email, senha)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Autenticação bem-sucedida
//                        val user = firebaseAuth.currentUser
//                        if (user != null) {
//                            // Busca o tipo de usuário no Firestore
//                            db.collection("users")
//                                .document(user.uid) // Use o UID do usuário autenticado
//                                .get()
//                                .addOnSuccessListener { document ->
//                                    if (document.exists()) {
//                                        val tipoUsuario = document.getString("tipoUsuario")
//                                        if (tipoUsuario == "Admin") {
//                                            // Redireciona para a tela de administrador
//                                            Toast.makeText(this, "Bem-vindo, Admin!", Toast.LENGTH_SHORT).show()
//                                            val intent = Intent(this, user_logged::class.java)
//                                            startActivity(intent)
//                                        } else {
//                                            // Redireciona para a tela padrão de usuário
//                                            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
//                                            val intent = Intent(this, homeScreen::class.java)
//                                            startActivity(intent)
//                                        }
//                                    } else {
//                                        // Usuário não encontrado no Firestore
//                                        Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                                .addOnFailureListener { exception ->
//                                    Log.w("Firestore", "Error getting documents.", exception)
//                                    Toast.makeText(this, "Erro ao acessar o banco de dados.", Toast.LENGTH_SHORT).show()
//                                }
//                        }
//                    } else {
//                        // Falha na autenticação
//                        Toast.makeText(this, "Falha no login. Verifique suas credenciais.", Toast.LENGTH_SHORT).show()
//                    }
                }
        }


