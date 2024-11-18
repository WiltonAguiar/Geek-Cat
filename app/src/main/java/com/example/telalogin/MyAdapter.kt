package com.example.telalogin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var list:List<String>):RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nome.text = list.get(position)

        holder.button.setOnClickListener{
            var intent = Intent(holder.itemView.context,user_logged::class.java)
            intent.putExtra("id",holder.nome.text)
            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context,list.get(position),Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MyViewHolder( item: View):RecyclerView.ViewHolder(item) {
    var nome:TextView
    var button:Button
    init {
        nome = item.findViewById(R.id.textView)
        button = item.findViewById(R.id.button)
    }
}
