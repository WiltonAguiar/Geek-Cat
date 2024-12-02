package com.example.telalogin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecyclerViewExemplo : AppCompatActivity() {
    private lateinit var recy: RecyclerView
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_exemplo)

        recy = findViewById(R.id.recyclerView)
        recy.layoutManager = LinearLayoutManager(this)

        fetchFasesData()
    }



    private fun fetchFasesData() {
        db.collection("Fases")
            .get()
            .addOnSuccessListener { documents ->
                // Cria o dataset contendo mapas com todos os dados necessários
                val dataset = mutableListOf<Map<String, Any>>()
                for (document in documents) {
                    val id = document.id // Obtém o ID do documento
                    val pergunta = document.getString("pergunta") ?: ""
                    val nivel = document.getString("nivel") ?: ""
                    val correta = document.get("correta") ?: emptyList<Any>() // Garante que seja uma lista ou vazio

                    dataset.add(
                        mapOf(
                            "id" to id,
                            "pergunta" to pergunta,
                            "nivel" to nivel,
                            "correta" to correta
                        )
                    )
                }

                // Atualiza o RecyclerView com o adaptador
                val adapter = MyAdapter(dataset)
                recy.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Erro ao buscar dados: ", exception)
            }
    }
}
