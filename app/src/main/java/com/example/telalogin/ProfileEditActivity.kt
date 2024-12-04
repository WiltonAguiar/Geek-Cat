import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.telalogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileEditActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val etName = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etConfPassword = findViewById<EditText>(R.id.et_confpassword)
        val btnSaveChanges = findViewById<Button>(R.id.btn_save_changes)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        btnSaveChanges.setOnClickListener {
            if (userId != null) {
                val updates = mapOf(
                    "nome" to etName.text.toString(),
                    "email" to etEmail.text.toString(),
                    "vida" to etPassword.text.toString().toIntOrNull(),
                    "score" to etConfPassword.text.toString().toIntOrNull()
                )

                db.collection("users").document(userId).update(updates)
                    .addOnSuccessListener {
                        Log.d("ProfileEdit", "Dados atualizados com sucesso!")
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.e("ProfileEdit", "Erro ao atualizar os dados: ", e)
                    }
            }
        }
    }
}
