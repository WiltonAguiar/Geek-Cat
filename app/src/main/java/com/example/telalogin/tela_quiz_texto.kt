package com.example.telalogin

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        // Firestore Instance
        val firestore = FirebaseFirestore.getInstance()

        // Busca uma fase aleatória de nível 1
        firestore.collection("Fases")
            .whereEqualTo("nivel", "1")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Nenhuma fase encontrada!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                // Seleciona um documento aleatório
                val randomDocument = documents.documents.random()

                // Extrai os dados da fase
                val pergunta = randomDocument.getString("pergunta")
                val respostas = randomDocument["correta"] as? List<Map<String, Any>>

                if (pergunta != null && respostas != null && respostas.size == 4) {
                    textoBanco.text = pergunta

                    // Define as alternativas
                    alternativa1.text = respostas[0]["resp1"] as? String ?: ""
                    alternativa2.text = respostas[1]["resp2"] as? String ?: ""
                    alternativa3.text = respostas[2]["resp3"] as? String ?: ""
                    alternativa4.text = respostas[3]["resp4"] as? String ?: ""

                    // Configura a verificação de respostas
                    val corretaIndex = respostas.indexOfFirst {
                        it["correta"] == true
                    }

                    if (corretaIndex == -1) {
                        Toast.makeText(this, "Erro: Nenhuma resposta correta definida!", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    // Listener para a alternativa 1
                    alternativa1.setOnClickListener {
                        verificarResposta(0, corretaIndex)
                    }

                    // Listener para a alternativa 2
                    alternativa2.setOnClickListener {
                        verificarResposta(1, corretaIndex)
                    }

                    // Listener para a alternativa 3
                    alternativa3.setOnClickListener {
                        verificarResposta(2, corretaIndex)
                    }

                    // Listener para a alternativa 4
                    alternativa4.setOnClickListener {
                        verificarResposta(3, corretaIndex)
                    }
                } else {
                    Toast.makeText(this, "Erro nos dados da fase!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar as fases: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Função para verificar a resposta
    private fun verificarResposta(indexSelecionado: Int, indexCorreto: Int) {
        if (indexSelecionado == indexCorreto) {
            Toast.makeText(this, "Resposta correta!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Resposta errada!", Toast.LENGTH_SHORT).show()
        }
    }
}
