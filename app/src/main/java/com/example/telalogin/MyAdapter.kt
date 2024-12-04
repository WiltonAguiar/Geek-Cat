package com.example.telalogin

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MyAdapter(private val list: List<Map<String, Any>>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]

        // Exibe o texto principal do item
        holder.nome.text = item["pergunta"].toString()

        // Configura o clique no botão "Deletar"
        holder.botaodeletar.setOnClickListener {
            val id = item["id"]?.toString()
            val contextdelete = holder.itemView.context
            val intentdelete = Intent(contextdelete, user_logged::class.java)

            if (id != null) {
                FirebaseFirestore.getInstance().collection("Fases").document(id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "Fase deletada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Atualiza a lista se necessário
                        // notifyItemRemoved(position)
                        contextdelete.startActivity(intentdelete)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            holder.itemView.context,
                            "Erro ao deletar a fase: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("MyAdapter", "Erro ao deletar o documento", e)
                    }
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "ID inválido para deletar",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        // Configura o clique no botão "Visualizar"
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
    val botaodeletar: Button = item.findViewById(R.id.BotaoDeletarFase)
}
