package com.example.telalogin


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val list: List<Map<String, Any>>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]

        // Exibe o texto principal do item
        holder.nome.text = item["pergunta"].toString()

        // Configura o clique no botão
        holder.button.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, LevelEditor::class.java)

            // Envia os dados do item para a LevelEditor
            intent.putExtra("id", item["id"].toString()) // ID do documento no Firestore
            intent.putExtra("pergunta", item["pergunta"].toString()) // Pergunta
            intent.putExtra("nivel", item["nivel"].toString()) // Nível de dificuldade
            intent.putExtra("correta", item["correta"].toString()) // Resposta correta (como JSON ou string)

            context.startActivity(intent) // Inicia a Activity
        }
    }

    override fun getItemCount(): Int = list.size
}

class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val nome: TextView = item.findViewById(R.id.Fase)
    val button: Button = item.findViewById(R.id.BotaoVisualizarFase)
}
