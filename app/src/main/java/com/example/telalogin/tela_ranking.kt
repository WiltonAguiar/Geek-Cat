package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class Jogador(
    val nome: String = "",
    val pontuacao: Int = 0
)

class tela_ranking : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_ranking)

        // Configurar RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_viewranking)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar lista de jogadores e adaptador
        val rankingList = mutableListOf<Jogador>()
        adapter = RankingAdapter(rankingList)
        recyclerView.adapter = adapter

        // Obter dados do Firestore
        db.collection("users")
            .orderBy("score", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                rankingList.clear() // Limpa a lista antes de adicionar novos dados
                for (document in result) {
                    val nome = document.getString("nome") ?: "Desconhecido"
                    val pontuacao = document.getLong("score")?.toInt() ?: 0
                    rankingList.add(Jogador(nome, pontuacao))
                }
                adapter.notifyDataSetChanged() // Atualiza o adaptador após adicionar os dados
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Erro ao carregar ranking", exception)
            }
    }
}
