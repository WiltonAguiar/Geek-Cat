package com.example.telalogin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.telalogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileEditActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        findViewById<EditText>(R.id.et_name).setText(document.getString("nome"))
                        findViewById<EditText>(R.id.et_email).setText(document.getString("email"))
                    }
                }
        }

        findViewById<Button>(R.id.btn_save_changes).setOnClickListener {
            val name = findViewById<EditText>(R.id.et_name).text.toString()
            val email = findViewById<EditText>(R.id.et_email).text.toString()

            val updates = mapOf(
                "nome" to name,
                "email" to email
            )

            db.collection("users").document(userId!!)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao atualizar os dados: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }

        findViewById<Button>(R.id.esqueceuSenha).setOnClickListener {
            val intent = Intent(this, forgot_password::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            finish()
        }
    }
}
