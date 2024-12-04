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

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvEmail = findViewById<TextView>(R.id.tv_email)
        val tvLife = findViewById<TextView>(R.id.tv_life)
        val tvScore = findViewById<TextView>(R.id.tv_score)
        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        tvName.text = "Nome: ${documentSnapshot.data?.get("nome") as String}"
                        tvEmail.text = "Email: ${documentSnapshot.data?.get("email") as String}"
                        tvLife.text = "Vida: ${documentSnapshot.data?.get("life") as String}"
                        tvScore.text = "Score: ${documentSnapshot.data?.get("score") as String}"
                    }

                }
                .addOnFailureListener { e ->
                    Log.e("ProfileView", "Erro ao buscar dados: ", e)
                }
        }

        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }
    }
}
