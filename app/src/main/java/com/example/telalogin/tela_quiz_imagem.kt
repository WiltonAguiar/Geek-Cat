package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tela_quiz_imagem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_quiz_imagem)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botaoCertaResposta = findViewById<Button>(R.id.alternativa2Imagem)
        val botaoRespostaErrada1 = findViewById<Button>(R.id.alternativa1Imagem)
        val botaoRespostaErrada2 = findViewById<Button>(R.id.alternativa3Imagem)
        val botaoRespostaErrada3 = findViewById<Button>(R.id.alternativa4Imagem)

        botaoCertaResposta.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Sucesso!")
                .setMessage("Certa resposta!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    val intent = Intent(this, homeScreen::class.java)
                    startActivity(intent)
                }
                .show()
        }

        botaoRespostaErrada1.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Errou")
                .setMessage("Resposta errada!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    val intent = Intent(this, homeScreen::class.java)
                    startActivity(intent)
                }
                .show()
        }

        botaoRespostaErrada2.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Errou")
                .setMessage("Resposta errada!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    val intent = Intent(this, homeScreen::class.java)
                    startActivity(intent)
                }
                .show()
        }

        botaoRespostaErrada3.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Errou")
                .setMessage("Resposta errada!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    val intent = Intent(this, homeScreen::class.java)
                    startActivity(intent)
                }
                .show()
        }
    }
}
