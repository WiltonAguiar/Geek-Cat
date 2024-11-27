package com.example.telalogin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var list: List<String>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nome.text = list[position]

        holder.button.setOnClickListener {
            val intent = Intent(holder.itemView.context, levelEditor::class.java) // Altere para LevelEditor
            intent.putExtra("id", holder.nome.text) // Passa o texto do item para a próxima Activity
            holder.itemView.context.startActivity(intent)

            // Opcional: Exibe uma mensagem para o item clicado
            Toast.makeText(holder.itemView.context, "Abrindo ${list[position]}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    var nome: TextView = item.findViewById(R.id.Fase)
    var button: Button = item.findViewById(R.id.BotaoVisualizarFase)
}
