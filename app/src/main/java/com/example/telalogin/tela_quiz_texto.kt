package com.example.telalogin

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class tela_quiz_texto : AppCompatActivity() {

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

        // Escuta as mudanças no documento do usuário para atualizar o score e life
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
            }
        }

        // Lógica do quiz e resposta (já implementada)
        firestore.collection("Fases")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Nenhuma fase encontrada!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                // Verifica se o usuário tem perguntas respondidas
                firestore.collection("users").document(userId).get()
                    .addOnSuccessListener { userDoc ->
                        val perguntasRespondidas = userDoc.get("respondidas") as? List<String> ?: emptyList()

                        // Verifica se a lista de respondidas não está vazia antes de aplicar o filtro
                        val query = if (perguntasRespondidas.isNotEmpty()) {
                            firestore.collection("Fases")
                                .whereNotIn("id", perguntasRespondidas) // Só aplica o filtro quando houver elementos na lista
                        } else {
                            firestore.collection("Fases") // Caso contrário, pega todas as fases
                        }

                        query.get()
                            .addOnSuccessListener { querySnapshot ->
                                if (querySnapshot.isEmpty) {
                                    Toast.makeText(this, "Nenhuma nova questão disponível.", Toast.LENGTH_SHORT).show()
                                    finish()
                                    return@addOnSuccessListener
                                }

                                // Continuar com a lógica do código, como antes
                                val randomQuestion = querySnapshot.documents.random()
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
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar as fases: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Função para configurar os click listeners nas alternativas
    private fun setupClickListeners(
        firestore: FirebaseFirestore,
        userId: String,
        questionId: String,
        corretaIndex: Int,
        alternativas: List<TextView>
    ) {
        alternativas[0].setOnClickListener {
            verificarResposta(0, corretaIndex, firestore, userId, questionId)
        }
        alternativas[1].setOnClickListener {
            verificarResposta(1, corretaIndex, firestore, userId, questionId)
        }
        alternativas[2].setOnClickListener {
            verificarResposta(2, corretaIndex, firestore, userId, questionId)
        }
        alternativas[3].setOnClickListener {
            verificarResposta(3, corretaIndex, firestore, userId, questionId)
        }
    }

    // Função para verificar a resposta
    private fun verificarResposta(indexSelecionado: Int, indexCorreto: Int, firestore: FirebaseFirestore, userId: String, questionId: String) {
        if (indexSelecionado == indexCorreto) {
            // Atualiza o score do usuário no Firestore
            val userRef = firestore.collection("users").document(userId)

            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentScore = document.getLong("score")?.toInt() ?: 0
                    val updatedScore = currentScore + 10  // Adiciona 10 pontos

                    userRef.update("score", updatedScore)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Resposta correta! +10 pontos.", Toast.LENGTH_SHORT).show()
                            // Marca a questão como respondida
                            userRef.update("respondidas", FieldValue.arrayUnion(questionId))
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar o score: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Usuário não encontrado no banco de dados!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Resposta errada, diminui 1 unidade de vida
            val userRef = firestore.collection("users").document(userId)

            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentLife = document.getLong("life")?.toInt() ?: 0
                    val updatedLife = currentLife - 1  // Diminui 1 vida

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
}
