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



data class Jogador(
    val nome: String,
    val pontuacao: Int
)

class tela_ranking : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_ranking)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = FirebaseFirestore.getInstance()
        val rankingList = mutableListOf<Jogador>()

        db.collection("ranking")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nome = document.getString("nome") ?: "Desconhecido"
                    val pontuacao = document.getString("pontuacao")?.toIntOrNull() ?: 0
                    rankingList.add(Jogador(nome, pontuacao))
                }
                // Atualize o adaptador com a lista de ranking
                val adapter = RankingAdapter(rankingList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Erro ao carregar o ranking: ", exception)
            }
    }
}