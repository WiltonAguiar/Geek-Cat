package com.example.telalogin

import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

class LevelEditor : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_editor)

        db = FirebaseFirestore.getInstance()


        val EnunciadoBanco: EditText = findViewById(R.id.EnunciadoBanco)
        val Pergunta1Banco: EditText = findViewById(R.id.Pergunta1Banco)
        val Pergunta2Banco: EditText = findViewById(R.id.Pergunta2Banco)
        val Pergunta3Banco: EditText = findViewById(R.id.Pergunta3Banco)
        val Pergunta4Banco: EditText = findViewById(R.id.Pergunta4Banco)

        val checkBoxPergunta1Banco: CheckBox = findViewById(R.id.checkBoxPergunta1Banco)
        val checkBoxPergunta2Banco: CheckBox = findViewById(R.id.checkBoxPergunta2Banco)
        val checkBoxPergunta3Banco: CheckBox = findViewById(R.id.checkBoxPergunta3Banco)
        val checkBoxPergunta4Banco: CheckBox = findViewById(R.id.checkBoxPergunta4Banco)

        val nivelDificuldade1Banco: CheckBox = findViewById(R.id.nivelDificuldade1Banco)
        val nivelDificuldade2Banco: CheckBox = findViewById(R.id.nivelDificuldade2Banco)
        val nivelDificuldade3Banco: CheckBox = findViewById(R.id.nivelDificuldade3Banco)

        // Recebe o ID da fase da Intent
        val faseId = intent.getStringExtra("id") ?: return

        // Carrega os dados da fase do Firestore
        db.collection("Fases").document(faseId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Puxa os dados do documento
                    val fase = documentSnapshot.data
                    val pergunta = fase?.get("pergunta") as String
                    val nivel = fase["nivel"] as String
                    val respostas = fase["correta"] as List<Map<String, Any>>

                    // Preenche os campos com os dados da fase
                    EnunciadoBanco.setText(pergunta)
                    Pergunta1Banco.setText(respostas[0]["resp1"].toString())
                    checkBoxPergunta1Banco.isChecked = respostas[0]["correta"] as Boolean

                    Pergunta2Banco.setText(respostas[1]["resp2"].toString())
                    checkBoxPergunta2Banco.isChecked = respostas[1]["correta"] as Boolean

                    Pergunta3Banco.setText(respostas[2]["resp3"].toString())
                    checkBoxPergunta3Banco.isChecked = respostas[2]["correta"] as Boolean

                    Pergunta4Banco.setText(respostas[3]["resp4"].toString())
                    checkBoxPergunta4Banco.isChecked = respostas[3]["correta"] as Boolean

                    // Define o nível de dificuldade
                    when (nivel) {
                        "1" -> nivelDificuldade1Banco.isChecked = true
                        "2" -> nivelDificuldade2Banco.isChecked = true
                        "3" -> nivelDificuldade3Banco.isChecked = true
                    }
                } else {
                    Toast.makeText(this, "Fase não encontrada!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar dados da fase: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        val botaoUpdateFase: Button = findViewById(R.id.BotaoUpdateFase)
        botaoUpdateFase.setOnClickListener {
            // Atualiza os dados no Firestore
            val faseAtualizada = mapOf(
                "pergunta" to EnunciadoBanco.text.toString(),
                "nivel" to when {
                    nivelDificuldade1Banco.isChecked -> "1"
                    nivelDificuldade2Banco.isChecked -> "2"
                    else -> "3"
                },
                "correta" to listOf(
                    mapOf("resp1" to Pergunta1Banco.text.toString(), "correta" to checkBoxPergunta1Banco.isChecked),
                    mapOf("resp2" to Pergunta2Banco.text.toString(), "correta" to checkBoxPergunta2Banco.isChecked),
                    mapOf("resp3" to Pergunta3Banco.text.toString(), "correta" to checkBoxPergunta3Banco.isChecked),
                    mapOf("resp4" to Pergunta4Banco.text.toString(), "correta" to checkBoxPergunta4Banco.isChecked)
                )
            )

            // Atualiza a fase no Firestore
            db.collection("Fases").document(faseId)
                .set(faseAtualizada)
                .addOnSuccessListener {
                    Toast.makeText(this, "Fase atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao atualizar a fase: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
