package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class homeScreen : AppCompatActivity() {

    private lateinit var fb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var userDocListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializando FirebaseAuth e FirebaseFirestore
        auth = FirebaseAuth.getInstance()
        fb = Firebase.firestore

        setupUserDataListener()

        // Navegação para outras telas
        findViewById<ImageView>(R.id.StartMission1).setOnClickListener {
            verificarVidas {
                val intent = Intent(this, tela_quiz_texto::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.imageButton_cup_gold).setOnClickListener {
            verificarVidas {
                val intent = Intent(this, tela_ranking::class.java)
                startActivity(intent)
            }
        }


        findViewById<ImageView>(R.id.StartMission2).setOnClickListener {
            verificarVidas {
                val intent = Intent(this, tela_quiz_texto_nv2::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.imagemFaseAleatoria).setOnClickListener {
            verificarVidas {
                val intent = Intent(this, tela_quiz_texto_aleatorio::class.java)
                startActivity(intent)
            }
        }



        findViewById<ImageView>(R.id.imageView5).setOnClickListener {
            verificarVidas {
                val intent = Intent(this, tela_quiz_texto_nv3::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.imageButton_heart_red).setOnClickListener {
            val intent = Intent(this, ShopScreen::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imageButton_gem_blue).setOnClickListener {
            val intent = Intent(this, ShopScreen::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imageButton_cup_gold).setOnClickListener {
            val intent = Intent(this, tela_ranking::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ranking).setOnClickListener {
            val intent = Intent(this, tela_ranking::class.java)
            startActivity(intent)
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

    private fun verificarVidas(onSuccess: () -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
        fb.collection("users").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val life = documentSnapshot.getLong("life") ?: 0
                if (life > 0) {
                    // Usuário tem vidas, pode continuar
                    onSuccess()
                } else {
                    // Usuário não tem vidas, mostra um alerta
                    AlertDialog.Builder(this)
                        .setTitle("Sem vidas!")
                        .setMessage("Você não tem vidas suficientes para acessar esta missão. Tente novamente mais tarde!")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancelable(false)
                        .show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao verificar vidas: ${e.message}", Toast.LENGTH_SHORT).show()
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
