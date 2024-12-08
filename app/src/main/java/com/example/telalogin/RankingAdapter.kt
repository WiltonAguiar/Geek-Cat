package com.example.telalogin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingAdapter(private val rankingList: List<Jogador>) :
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeTextView: TextView = view.findViewById(R.id.nome)
        val pontuacaoTextView: TextView = view.findViewById(R.id.pontuacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false) // Alterar para o layout do item
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val jogador = rankingList[position]
        holder.nomeTextView.text = jogador.nome
        holder.pontuacaoTextView.text = jogador.pontuacao.toString()
    }

    override fun getItemCount(): Int = rankingList.size
}
