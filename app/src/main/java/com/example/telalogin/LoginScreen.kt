package com.example.telalogin

import ProfileViewActivity
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

    private lateinit var auth: FirebaseAuth

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
        firebaseAuth = FirebaseAuth.getInstance()//ok

//        auth = FirebaseAuth.getInstance()
//        val emailInput = findViewById<EditText>(R.id.Email)
//        val passwordInput = findViewById<EditText>(R.id.Senha)
//        val loginButton = findViewById<Button>(R.id.botaoEntrar)
//        val registerButton = findViewById<Button>(R.id.registrar)

        // Botão para esquecimento de senha
        findViewById<Button>(R.id.esqueceuSenha).setOnClickListener {
            val intent = Intent(this, forgot_password::class.java)
            startActivity(intent)//ok
        }

        // Botão para registrar novo usuário
        findViewById<Button>(R.id.registrar).setOnClickListener {
            val intent = Intent(this, register_screen::class.java)
            startActivity(intent)//ok
        }

//        loginButton.setOnClickListener {
//            val email = emailInput.text.toString().trim()
//            val password = passwordInput.text.toString().trim()
//
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                loginUser(email, password)
//            } else {
//                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
//            }
//        }

        private fun loginUser(email: String, password: String) {
           firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Verifica o tipo de usuário no Firestore
                         db.collection("users").document(firebaseAuth.currentUser?.uid).get()
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
                                            Toast.makeText(this,"Usuário não encontrado.",Toast.LENGTH_SHORT).show()
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
                        // Falha no login
                        Toast.makeText(this, "Erro ao fazer login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Login", "Erro ao autenticar usuário", task.exception)
                    }
                }
        }


        buttonLogin.setOnClickListener {
            val email = editTextLoginEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

//            } else if (email == "Admin@gmail.com" && senha == "1234") { // Verifica se o usuário é o administrador com email e senha específicos
//                Toast.makeText(this, "Bem-vindo, Admin!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, user_logged::class.java)
//                startActivity(intent)//ok

            } else {
                // Autenticação com Firebase para outros usuários
                                loginUser(email, senha)
//                firebaseAuth.signInWithEmailAndPassword(email, senha)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//
//                            val user = firebaseAuth.currentUser
//
//                            if (user != null) {
//
//                                // Verifica o tipo de usuário no Firestore
//                                db.collection("users").document(user.uid).get()
//                                    .addOnSuccessListener { document ->
//                                        if (document.exists()) {
//                                            val tipoUsuario = document.getString("tipoUsuario")
//                                            if (tipoUsuario == "Admin") {
//                                                Toast.makeText(
//                                                    this,
//                                                    "Bem-vindo, Admin!",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                                val intent = Intent(this, user_logged::class.java)
//                                                startActivity(intent)
//                                            } else {
//                                                // Usuário padrão encontrado, redireciona para homeScreen
//                                                Toast.makeText(
//                                                    this,
//                                                    "Login bem-sucedido!",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                                val intent = Intent(this, homeScreen::class.java)
//                                                startActivity(intent)
//                                            }
//                                        } else {
//                                            Toast.makeText(
//                                                this,
//                                                "Usuário não encontrado.",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    }
//                                    .addOnFailureListener { exception ->
//                                        Log.w("Firestore", "Erro ao acessar o banco de dados.", exception)
//                                        Toast.makeText(
//                                            this,
//                                            "Erro ao acessar o banco de dados.",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                            }
//                        } else {
//                            // Caso não seja autenticado pelo Firebase, faz a verificação manual no Firestore
//                            db.collection("users")
//                                .whereEqualTo("email", email)
//                                .whereEqualTo("senha", senha)
//                                .get()
//                                .addOnSuccessListener { documents ->
//                                    if (!documents.isEmpty) {
//                                        // Se encontrar o usuário com email e senha corretos
//                                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
//                                        val intent = Intent(this, homeScreen::class.java)
//                                        startActivity(intent)
//                                    } else {
//                                        Toast.makeText(this, "Credenciais inválidas.", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                                .addOnFailureListener { exception ->
//                                    Log.w("Firestore", "Erro ao acessar o banco de dados.", exception)
//                                    Toast.makeText(this, "Erro ao acessar o banco de dados.", Toast.LENGTH_SHORT).show()
//                                }
//                        }
//                    }
            }
        }
    }
}
