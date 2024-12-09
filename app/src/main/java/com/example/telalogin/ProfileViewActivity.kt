package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.telalogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)

        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        findViewById<TextView>(R.id.tv_name).text = document.getString("nome")
                        findViewById<TextView>(R.id.tv_email).text = document.getString("email")
                        findViewById<TextView>(R.id.tv_life).text = document.getLong("life")?.toString()
                        findViewById<TextView>(R.id.tv_score).text = document.getLong("score")?.toString()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao carregar os dados: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }

        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }
    }
}
