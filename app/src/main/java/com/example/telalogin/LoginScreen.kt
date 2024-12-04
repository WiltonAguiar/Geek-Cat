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
        setContentView(R.layout.activity_login_screen)

        editTextLoginEmail = findViewById(R.id.Email)
        editTextSenha = findViewById(R.id.Senha)
        buttonLogin = findViewById(R.id.botaoEntrar)
        firebaseAuth = FirebaseAuth.getInstance()

        // Botão para esquecimento de senha
        findViewById<Button>(R.id.esqueceuSenha).setOnClickListener {
            val intent = Intent(this, forgot_password::class.java)
            startActivity(intent)
        }

        // Botão para registrar novo usuário
        findViewById<Button>(R.id.registrar).setOnClickListener {
            val intent = Intent(this, register_screen::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val email = editTextLoginEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifica se o usuário é o administrador com email e senha específicos
            if (email == "Admin@gmail.com" && senha == "1234") {
                Toast.makeText(this, "Bem-vindo, Admin!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, user_logged::class.java)
                startActivity(intent)
            } else {
                // Autenticação com Firebase para outros usuários
                firebaseAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                // Verifica o tipo de usuário no Firestore
                                db.collection("users")
                                    .document(user.uid)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            val tipoUsuario = document.getString("tipoUsuario")
                                            if (tipoUsuario == "Admin") {
                                                Toast.makeText(
                                                    this,
                                                    "Bem-vindo, Admin!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(this, user_logged::class.java)
                                                startActivity(intent)
                                            } else {
                                                // Usuário padrão encontrado, redireciona para homeScreen
                                                Toast.makeText(
                                                    this,
                                                    "Login bem-sucedido!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(this, homeScreen::class.java)
                                                startActivity(intent)
                                            }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Usuário não encontrado.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w("Firestore", "Erro ao acessar o banco de dados.", exception)
                                        Toast.makeText(
                                            this,
                                            "Erro ao acessar o banco de dados.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            // Caso não seja autenticado pelo Firebase, faz a verificação manual no Firestore
                            db.collection("users")
                                .whereEqualTo("email", email)
                                .whereEqualTo("senha", senha)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (!documents.isEmpty) {
                                        // Se encontrar o usuário com email e senha corretos
                                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, homeScreen::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this, "Credenciais inválidas.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("Firestore", "Erro ao acessar o banco de dados.", exception)
                                    Toast.makeText(this, "Erro ao acessar o banco de dados.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
            }
        }
    }
}