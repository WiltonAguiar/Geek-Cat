package com.example.telalogin

import android.content.Intent
import android.os.Bundle

import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class level_creator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level_creator)


        lateinit var fb: FirebaseFirestore
        lateinit var Enunciado: EditText
        lateinit var resposta1: EditText
        lateinit var resposta2:EditText
        lateinit var resposta3: EditText
        lateinit var resposta4: EditText
        lateinit var checkBoxPergunta1:CheckBox
        lateinit var checkBoxPergunta2:CheckBox
        lateinit var checkBoxPergunta3:CheckBox
        lateinit var checkBoxPergunta4:CheckBox
        lateinit var BotaoFinalizarCriacaoFase:Button

        lateinit var nivelDificuldade1:CheckBox
        lateinit var nivelDificuldade2:CheckBox
        lateinit var nivelDificuldade3:CheckBox

        Enunciado = findViewById(R.id.Enunciado)
        resposta1 = findViewById(R.id.Pergunta1)
        resposta2 = findViewById(R.id.Pergunta2)
        resposta3 = findViewById(R.id.Pergunta3)
        resposta4 = findViewById(R.id.Pergunta4)
        checkBoxPergunta1 = findViewById(R.id.checkBoxPergunta1)
        checkBoxPergunta2 = findViewById(R.id.checkBoxPergunta2)
        checkBoxPergunta3 = findViewById(R.id.checkBoxPergunta3)
        checkBoxPergunta4 = findViewById(R.id.checkBoxPergunta4)
        BotaoFinalizarCriacaoFase = findViewById(R.id.BotaoFinalizarCriacaoFase)
        fb = Firebase.firestore
        nivelDificuldade1 = findViewById(R.id.nivelDificuldade1)

        nivelDificuldade2 = findViewById(R.id.nivelDificuldade2)

        nivelDificuldade3 = findViewById(R.id.nivelDificuldade3)

        val botaoFinalizarCriacaoFase = findViewById<Button>(R.id.BotaoFinalizarCriacaoFase)


        botaoFinalizarCriacaoFase.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Sucesso!")
                .setMessage("Fase criada com sucesso!")
                .setPositiveButton("OK") { dialog, _ ->

                    dialog.dismiss()
                    val intent = Intent(this, user_logged::class.java)
                    startActivity(intent)
                }
                .show()
        }


        botaoFinalizarCriacaoFase.setOnClickListener {
            val nivelSelecionado = when {
                nivelDificuldade1.isChecked -> "1"
                nivelDificuldade2.isChecked -> "2"
                nivelDificuldade3.isChecked -> "3"
                else -> null
            }

            if (nivelSelecionado == null) {
                Toast.makeText(this, "Selecione um nível!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val certaResposta = listOf(
                mapOf("resp1" to resposta1.text.toString(), "correta" to checkBoxPergunta1.isChecked),
                mapOf("resp2" to resposta2.text.toString(), "correta" to checkBoxPergunta2.isChecked),
                mapOf("resp3" to resposta3.text.toString(), "correta" to checkBoxPergunta3.isChecked),
                mapOf("resp4" to resposta4.text.toString(), "correta" to checkBoxPergunta4.isChecked)
            )

            val fase = mapOf(
                "pergunta" to Enunciado.text.toString(),
                "nivel" to nivelSelecionado,
                "correta" to certaResposta
            )


            FirebaseFirestore.getInstance().collection("Fases")
                .add(fase)
                .addOnSuccessListener {
                    Toast.makeText(this, "Fase criada com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao criar a fase: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    }

