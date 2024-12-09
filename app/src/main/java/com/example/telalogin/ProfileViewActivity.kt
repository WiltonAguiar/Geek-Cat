package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.telalogin.R
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

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)

        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        findViewById<TextView>(R.id.tv_name).text = document.getString("nome")
                        findViewById<TextView>(R.id.tv_email).text = document.getString("email")
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao carregar os dados: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }

        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }
    }

    private fun setupUserDataListener() {

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
        val userDocRef = fb.collection("users").document(userId)

        // Cancelar o listener anterior, se existir
        userDocListener?.remove()

        // Configurando TextViews
        val scoreTextView: TextView = findViewById(R.id.counter_cup_gold)
        val lifeTextView: TextView = findViewById(R.id.counter_heart_red)

        // Configurando um novo listener para o documento do usuário
        userDocListener = userDocRef.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Toast.makeText(this, "Erro ao escutar atualizações: ${e.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val score = documentSnapshot.getLong("score") ?: 0
                val life = documentSnapshot.getLong("life") ?: 0

                // Atualizando os valores na interface
                scoreTextView.text = score.toString()
                lifeTextView.text = life.toString()
            } else {
                Toast.makeText(this, "Dados do usuário não encontrados!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupUserDataListener()
    }

    override fun onStop() {
        super.onStop()
        // Remove o listener ao sair da tela
        userDocListener?.remove()
    }
}
