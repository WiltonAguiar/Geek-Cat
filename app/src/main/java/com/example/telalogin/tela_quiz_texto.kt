package com.example.telalogin

import android.os.Bundle
import android.util.Log
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
                    alternativa1.text = respostas[0]["resp1"] as? String ?: ""
                    alternativa2.text = respostas[1]["resp2"] as? String ?: ""
                    alternativa3.text = respostas[2]["resp3"] as? String ?: ""
                    alternativa4.text = respostas[3]["resp4"] as? String ?: ""

                    Toast.makeText(this, "Fase carregada com sucesso:", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro nos dados da fase!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar as fases: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
