package com.example.telalogin


import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore



data class Jogador(val nome: String, val pontuacao: Int)

class tela_ranking : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_ranking)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Dados de exemplo para o ranking
        val rankingList = listOf(
            Jogador("Alice", 120),
            Jogador("Bob", 95),
            Jogador("Carol", 85)
        )

        val adapter = RankingAdapter(rankingList)
        recyclerView.adapter = adapter

//        val db = FirebaseFirestore.getInstance()
//
//        val rankingList = mutableListOf<Jogador>()
//
//        db.collection("ranking")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val nome = document.getString("nome") ?: "Desconhecido"
//                    val pontuacao = document.getLong("pontuacao")?.toInt() ?: 0
//                    rankingList.add(Jogador(nome, pontuacao))
//                }
//                val adapter = RankingAdapter(rankingList)
//                recyclerView.adapter = adapter
//            }
//            .addOnFailureListener { exception ->
//                Log.w("Firebase", "Erro ao carregar o ranking.", exception)
//            }


    }

//    private fun fetchRanking(jogadores: MutableList<Jogador>) {
//        db.collection("ranking")
//            .orderBy("pontuacao", com.google.firebase.firestore.Query.Direction.DESCENDING)
//            .get()
//            .addOnSuccessListener { result ->
//                jogadores.clear()
//                for (document in result) {
//                    val nome = document.getString("nome") ?: ""
//                    val pontuacao = document.getLong("pontuacao")?.toInt() ?: 0
//                    jogadores.add(Jogador(nome, pontuacao))
//                }
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                // Log do erro ou exibição de mensagem para o usuário
//                exception.printStackTrace()
//            }
//    }
}