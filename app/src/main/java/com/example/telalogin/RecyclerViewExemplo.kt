package com.example.telalogin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewExemplo : AppCompatActivity() {
    lateinit var recy:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_exemplo)
        recy = findViewById(R.id.recyclerView)

        var dataset = listOf("Narak","Davi","Wilton","Mayra","Narak","Davi","Wilton","Mayra","Narak","Davi","Wilton","Mayra","Narak","Davi","Wilton","Mayra")

        var adapter = MyAdapter(dataset)
        recy.layoutManager = LinearLayoutManager(this)
        recy.adapter = adapter
    }
}