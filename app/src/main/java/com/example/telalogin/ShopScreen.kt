package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity

class ShopScreen : AppCompatActivity() {

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

        // Configura o botão de sair para retornar à tela inicial
        findViewById<Button>(R.id.button_sair_loja).setOnClickListener {
            val intent = Intent(this, homeScreen::class.java)
            startActivity(intent)
        }
    }
}
