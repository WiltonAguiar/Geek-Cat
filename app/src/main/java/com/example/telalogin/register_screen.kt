package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class register_screen : AppCompatActivity() {

    lateinit var fb: FirebaseFirestore
    lateinit var editTextUsuario: EditText
    lateinit var editTextEmail: EditText
    lateinit var buttonRegistrar: Button
    lateinit var editTextSenha: EditText
    lateinit var editTextConfirmaSenha: EditText
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)

        editTextUsuario = findViewById(R.id.usuario)
        editTextEmail = findViewById(R.id.emailRegistro)
        buttonRegistrar = findViewById(R.id.botaoRegistrar)
        editTextSenha = findViewById(R.id.senharegistro)
        editTextConfirmaSenha = findViewById(R.id.repeatSenha)

        fb = Firebase.firestore
        auth = FirebaseAuth.getInstance() // Instancia o FirebaseAuth

        findViewById<Button>(R.id.voltarTelaLogin).setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }

        buttonRegistrar.setOnClickListener {
            val nome = editTextUsuario.text.toString()
            val email = editTextEmail.text.toString()
            val senha = editTextSenha.text.toString()
            val confirmaSenha = editTextConfirmaSenha.text.toString()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("Todos os campos são obrigatórios!")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
                return@setOnClickListener
            }

            if (senha != confirmaSenha) {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("As senhas não coincidem!")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
                return@setOnClickListener
            }

            // Registra o usuário no Firebase Authentication
            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Usuário autenticado com sucesso, agora salva os dados no Firestore
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val userData = mapOf(
                            "nome" to nome,
                            "email" to email,
                            "senha" to senha,
                            "life" to 5,
                            "score" to 0
                        )

                        fb.collection("users")
                            .document(userId) // Usa o UID do Firebase Authentication como documento no Firestore
                            .set(userData)
                            .addOnSuccessListener {
                                AlertDialog.Builder(this)
                                    .setTitle("Sucesso!")
                                    .setMessage("Registro realizado com sucesso!")
                                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                    .show()
                                // Redireciona para a tela de login após registro
                                val intent = Intent(this, LoginScreen::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                AlertDialog.Builder(this)
                                    .setTitle("Erro")
                                    .setMessage("Erro ao registrar usuário no Firestore: ${e.message}")
                                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                    .show()
                                Log.e("Firestore", "Erro ao registrar usuário no Firestore", e)
                            }
                    } else {
                        // Se falhar ao criar o usuário no Firebase Authentication
                        AlertDialog.Builder(this)
                            .setTitle("Erro")
                            .setMessage("Erro ao registrar usuário: ${task.exception?.message}")
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                }
        }
    }
}
