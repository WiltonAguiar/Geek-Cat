package com.example.telalogin

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class tela_quiz_texto_nv3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_quiz_texto)

        // IDs dos TextViews
        val textoBanco: TextView = findViewById(R.id.TextoBanco)
        val alternativa1: TextView = findViewById(R.id.alternativa1Texto)
        val alternativa2: TextView = findViewById(R.id.alternativa2Texto)
        val alternativa3: TextView = findViewById(R.id.alternativa3Texto)
        val alternativa4: TextView = findViewById(R.id.alternativa4Texto)
        val scoreTextView: TextView = findViewById(R.id.counter_cup_gold)  // ID do score
        val lifeTextView: TextView = findViewById(R.id.counter_heart_red)  // ID da vida

        // Firestore Instance
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        // Verifica se o usuário está autenticado
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show()
            return
        }

        // Escuta as mudanças no documento do usuário para atualizar o score e vida
        firestore.collection("users").document(userId).addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Toast.makeText(this, "Erro ao atualizar dados: ${e.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val score = documentSnapshot.getLong("score") ?: 0
                val life = documentSnapshot.getLong("life") ?: 0

                // Atualiza os valores na interface
                scoreTextView.text = score.toString()
                lifeTextView.text = life.toString()

                // Verifica se a vida é 0 e impede a entrada na tela do quiz
                if (life <= 0) {
                    mostrarDialogFimDeJogo()
                    return@addSnapshotListener // Impede a execução de código posterior
                }
            }
        }

        // Lógica do quiz e resposta
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { userDoc ->
                val perguntasRespondidas = userDoc.get("respondidas") as? List<String> ?: emptyList()
                val nivelDesejado = "3" // Filtro para nível 1

                // Carrega todas as questões de nível 1
                firestore.collection("Fases")
                    .whereEqualTo("nivel", nivelDesejado)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            Toast.makeText(this, "Nenhuma nova questão disponível.", Toast.LENGTH_SHORT).show()
                            finish()
                            return@addOnSuccessListener
                        }

                        // Filtra localmente as questões que já foram respondidas
                        val perguntasNaoRespondidas = querySnapshot.documents.filter { document ->
                            !perguntasRespondidas.contains(document.id)
                        }

                        if (perguntasNaoRespondidas.isEmpty()) {
                            Toast.makeText(this, "Nenhuma nova questão disponível.", Toast.LENGTH_SHORT).show()
                            finish()
                            return@addOnSuccessListener
                        }

                        val randomQuestion = perguntasNaoRespondidas.random()
                        val pergunta = randomQuestion.getString("pergunta") ?: "Pergunta indisponível"
                        val respostas = randomQuestion["correta"] as? List<Map<String, Any>> ?: emptyList()

                        if (respostas.size == 4) {
                            textoBanco.text = pergunta
                            alternativa1.text = respostas[0]["resp1"] as? String ?: ""
                            alternativa2.text = respostas[1]["resp2"] as? String ?: ""
                            alternativa3.text = respostas[2]["resp3"] as? String ?: ""
                            alternativa4.text = respostas[3]["resp4"] as? String ?: ""

                            val corretaIndex = respostas.indexOfFirst { it["correta"] == true }
                            if (corretaIndex != -1) {
                                setupClickListeners(
                                    firestore,
                                    userId,
                                    randomQuestion.id,
                                    corretaIndex,
                                    listOf(alternativa1, alternativa2, alternativa3, alternativa4)
                                )
                            } else {
                                Toast.makeText(this, "Erro nos dados da questão.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "Erro no formato das respostas.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao carregar questões: ${e.message}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar dados do usuário: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupClickListeners(
        firestore: FirebaseFirestore,
        userId: String,
        questionId: String,
        corretaIndex: Int,
        alternativas: List<TextView>
    ) {
        alternativas.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                verificarResposta(index, corretaIndex, firestore, userId, questionId)
            }
        }
    }

    private fun verificarResposta(indexSelecionado: Int, indexCorreto: Int, firestore: FirebaseFirestore, userId: String, questionId: String) {
        if (indexSelecionado == indexCorreto) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentScore = document.getLong("score")?.toInt() ?: 0
                    val updatedScore = currentScore + 30

                    userRef.update("score", updatedScore)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Resposta correta! +30 pontos.", Toast.LENGTH_SHORT).show()
                            userRef.update("respondidas", FieldValue.arrayUnion(questionId))
                                .addOnSuccessListener {
                                    finish()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar o score: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        } else {
            val userRef = firestore.collection("users").document(userId)

            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentLife = document.getLong("life")?.toInt() ?: 0
                    val updatedLife = currentLife - 1

                    userRef.update("life", updatedLife)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Resposta errada! -1 vida.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar a vida: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun mostrarDialogFimDeJogo() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Fim de Jogo")
            .setMessage("Você não tem mais vidas. Deseja sair?")
            .setPositiveButton("OK") { _, _ -> finish() }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }
}
