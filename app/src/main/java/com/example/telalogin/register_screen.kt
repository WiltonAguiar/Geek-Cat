package com.example.telalogin

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class register_screen : AppCompatActivity() {

    lateinit var fb: FirebaseFirestore
    lateinit var editTextUsuario:EditText
    lateinit var editTextEmail:EditText
    lateinit var buttonRegistrar:Button
    lateinit var editTextSenha:EditText
    lateinit var editTextConfirmaSenha:EditText
    lateinit var editTextTipoUsuario:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)


        editTextUsuario = findViewById(R.id.usuario)
        editTextEmail = findViewById(R.id.emailRegistro)
        buttonRegistrar = findViewById(R.id.botaoRegistrar)
        editTextSenha = findViewById(R.id.senharegistro)
        editTextConfirmaSenha = findViewById(R.id.repeatSenha)

        fb = Firebase.firestore
        findViewById<Button>(R.id.voltarTelaLogin).setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }

        val BotaoRegistrarSenha = findViewById<Button>(R.id.botaoRegistrar)

        BotaoRegistrarSenha.setOnClickListener {
            Log.d("Firestore","usuario:"+ editTextUsuario.text)
            Log.d("Firestore","email:"+ editTextEmail.text)
            Log.d("Firestore","senha:"+ editTextSenha.text)
            Log.d("Firestore","confirmar senha:"+ editTextConfirmaSenha.text)


            fb.collection("users")
                .add(mapOf(
                "nome" to editTextUsuario.text.toString(),
                "email" to editTextEmail.text.toString(),
                "senha" to editTextSenha.text.toString(),
                "confirmarsenha" to editTextConfirmaSenha.text.toString()

            ))


            AlertDialog.Builder(this)
                .setTitle("Sucesso!")
                .setMessage("Registro realizado!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()

                }
                .show()
        }



    }


}