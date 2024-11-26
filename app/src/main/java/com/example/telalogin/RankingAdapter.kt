package com.example.telalogin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_layout.view.*

class RankingAdapter(private val jogadores: List<Jogador>) : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    //q poha é um viewholder???
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogador = jogadores[position]
        holder.itemView.nome.text = jogador.nome
        holder.itemView.pontuacao.text = jogador.pontuacao.toString()
    }

    override fun getItemCount(): Int = jogadores.size
}