package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ProfileViewActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fb: FirebaseFirestore
    private var userDocListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        auth = FirebaseAuth.getInstance()
        fb = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val userId = currentUser.uid
        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)
        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvEmail = findViewById<TextView>(R.id.tv_email)

        // Carregar dados do usuário
        fb.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    tvName.text = document.getString("nome") ?: "Nome não disponível"
                    tvEmail.text = document.getString("email") ?: "Email não disponível"
                } else {
                    Toast.makeText(this, "Dados do usuário não encontrados!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        // Editar perfil
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }
    }

    private fun setupUserDataListener() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
        val userDocRef = fb.collection("users").document(userId)

        // Configurar TextViews
        val scoreTextView = findViewById<TextView>(R.id.counter_cup_gold)
        val lifeTextView = findViewById<TextView>(R.id.counter_heart_red)

        // Cancelar listener anterior, se existir
        userDocListener?.remove()

        // Configurar novo listener
        userDocListener = userDocRef.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Log.e("ProfileViewActivity", "Erro ao escutar atualizações: ${e.message}")
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val score = documentSnapshot.getLong("score") ?: 0
                val life = documentSnapshot.getLong("life") ?: 0

                // Atualizar valores na interface
                scoreTextView.text = score.toString()
                lifeTextView.text = life.toString()
            } else {
                Log.w("ProfileViewActivity", "Documento do usuário não encontrado!")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupUserDataListener()
    }

    override fun onStop() {
        super.onStop()
        // Remover o listener ao sair da tela
        userDocListener?.remove()
    }
}