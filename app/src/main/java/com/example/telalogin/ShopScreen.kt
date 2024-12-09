package com.example.telalogin


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ListenerRegistration

class ShopScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fb: FirebaseFirestore
    private var userDocListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_screen)

        // Instâncias do Firestore e FirebaseAuth
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        // Obtém o ID do usuário autenticado
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        // Configura o ImageButton para aumentar a vida
        findViewById<ImageButton>(R.id.imageButton6).setOnClickListener {
            // Acessa o documento do usuário no Firestore
            val userRef = firestore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val currentLife = document.getLong("life")?.toInt() ?: 0
                        if (currentLife < 5) { // Verifica se a vida é menor que o máximo permitido
                            val updatedLife = currentLife + 1

                            // Atualiza o campo "life" no Firestore
                            userRef.update("life", updatedLife)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Vida aumentada! Novo valor: $updatedLife", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Erro ao atualizar vida: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Você já está com o máximo de vidas!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Usuário não encontrado no Firestore.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao acessar dados do usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            finish() // Encerra a Activity atual e volta para a anterior
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
