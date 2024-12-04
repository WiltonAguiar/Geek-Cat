package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)

        editTextUsuario = findViewById(R.id.usuario)
        editTextEmail = findViewById(R.id.emailRegistro)
        buttonRegistrar = findViewById(R.id.botaoRegistrar)
        editTextSenha = findViewById(R.id.senharegistro)
        editTextConfirmaSenha = findViewById(R.id.repeatSenha)

        fb = Firebase.firestore
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

            // Adiciona os dados do usuário com valores padrão para life e score
            val userData = mapOf(
                "nome" to nome,
                "email" to email,
                "senha" to senha,
                "life" to 5,
                "score" to 0
            )

            fb.collection("users")
                .add(userData)
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("Sucesso!")
                        .setMessage("Registro realizado com sucesso!")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
                .addOnFailureListener { e ->
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("Erro ao registrar usuário: ${e.message}")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                    Log.e("Firestore", "Erro ao registrar usuário", e)
                }
        }
    }
}
