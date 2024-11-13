package com.example.telalogin

import android.content.Intent
import android.os.Bundle

import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
        lateinit var Pergunta1: EditText
        lateinit var Pergunta2:EditText
        lateinit var Pergunta3: EditText
        lateinit var Pergunta4: EditText
        lateinit var checkBoxPergunta1:CheckBox
        lateinit var checkBoxPergunta2:CheckBox
        lateinit var checkBoxPergunta3:CheckBox
        lateinit var checkBoxPergunta4:CheckBox
        lateinit var BotaoFinalizarCriacaoFase:Button

        Enunciado = findViewById(R.id.Enunciado)
        Pergunta1 = findViewById(R.id.Pergunta1)
        Pergunta2 = findViewById(R.id.Pergunta2)
        Pergunta3 = findViewById(R.id.Pergunta3)
        Pergunta4 = findViewById(R.id.Pergunta4)
        checkBoxPergunta1 = findViewById(R.id.checkBoxPergunta1)
        checkBoxPergunta2 = findViewById(R.id.checkBoxPergunta2)
        checkBoxPergunta3 = findViewById(R.id.checkBoxPergunta3)
        checkBoxPergunta4 = findViewById(R.id.checkBoxPergunta4)
        BotaoFinalizarCriacaoFase = findViewById(R.id.BotaoFinalizarCriacaoFase)
        fb = Firebase.firestore


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

    }
}
