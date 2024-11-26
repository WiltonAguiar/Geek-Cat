package com.example.telalogin

import CustomAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_tela_ranking.*


data class Jogador(val nome: String = "", val pontuacao: Int = 0)

class tela_ranking : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: RankingAdapter

    private lateinit var recycler_view


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_ranking)

        val jogadores = mutableListOf<Jogador>()
        adapter = RankingAdapter(jogadores)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter

        fetchRanking(jogadores)

//        val dataset = arrayOf("January", "February", "March")
//        val customAdapter = CustomAdapter(dataset)
//        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = customAdapter
    }

    private fun fetchRanking(jogadores: MutableList<Jogador>) {
        db.collection("ranking")
            .orderBy("pontuacao", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                jogadores.clear()
                for (document in result) {
                    val nome = document.getString("nome") ?: ""
                    val pontuacao = document.getLong("pontuacao")?.toInt() ?: 0
                    jogadores.add(Jogador(nome, pontuacao))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Log do erro ou exibição de mensagem para o usuário
                exception.printStackTrace()
            }
    }
}