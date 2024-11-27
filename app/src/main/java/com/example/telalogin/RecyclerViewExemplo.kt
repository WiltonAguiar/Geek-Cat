package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.telalogin.R.id.BotaoVisualizarFase
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
                val dataset = mutableListOf<String>()
                for (document in documents) {
                    val fase = document.getString("pergunta") // Substitua "nome" pela chave usada no Firestore
                    if (fase != null) {
                        dataset.add(fase)
                    }
                }
                // Atualiza o RecyclerView com os dados
                val adapter = MyAdapter(dataset)
                recy.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Erro ao buscar dados: ", exception)
            }
    }
}
